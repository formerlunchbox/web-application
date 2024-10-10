package com.formerlunchbox.web.limiter.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//用于在 Spring 容器完成对 bean 的属性注入后执行一些初始化逻辑。
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import com.formerlunchbox.web.limiter.LimiterAbstract;
import com.formerlunchbox.web.limiter.propertities.RedisRateLimitProperties;
import com.formerlunchbox.web.tool.ScriptHolder;
import com.google.common.collect.ImmutableList;

public class RedisRateLimiter implements LimiterAbstract, InitializingBean {
  private static final Logger log = LoggerFactory.getLogger(RedisRateLimiter.class);

  private RedisTemplate<String, Object> redisTemplate;
  private RedisRateLimitProperties redisRateLimitProperties;
  /**
   * 限流器的redis key 前缀
   */
  private static final String RATE_LIMITER_KEY_PREFIX = "rate_limiter:";
  public static final String RATE_LIMITER_LUA_SHA_1 = RATE_LIMITER_KEY_PREFIX + "sha1";

  // lua 脚本的实例
  private RedisScript<Long> rateLimiterScript = null;

  public RedisRateLimiter(RedisTemplate redisTemplate, RedisRateLimitProperties redisRateLimitProperties) {

    this.redisTemplate = redisTemplate;
    rateLimiterScript = ScriptHolder.getRateLimitScript();
    this.redisRateLimitProperties = redisRateLimitProperties;
  }

  private Map<String, LimiterInfo> limiterInfoMap = new HashMap<>();

  @Override
  public Boolean tryAcquire(String cacheKey) {
    if (cacheKey == null) {
      return true;
    }
    if (cacheKey.indexOf(":") <= 0) {
      cacheKey = "default:" + cacheKey;
    }
    LimiterInfo limiterInfo = limiterInfoMap.get(cacheKey);
    if (limiterInfo == null) {
      return true;
    }

    Long acquire = (Long) redisTemplate.execute(rateLimiterScript,
        ImmutableList.of(limiterInfo.fullKey()),
        "acquire",
        "1");

    return acquire != 1;
  }

  /**
   * 加载配置文件中的限速配置 limiterInfos可以写到配置文件中，利用ConfigurationProperties装配
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    List<LimiterInfo> limiterInfos = redisRateLimitProperties.getLimiterInfos();
    // executorService.scheduleAtFixedRate(new Runnable()
    // {
    // @Override
    // public void run()
    // {
    try {
      if (null == limiterInfos) {
        return;
      }
      for (LimiterInfo limiterInfo : limiterInfos) {
        initLimitKey(limiterInfo);
      }
      log.info("redis rate limit inited !");
    } catch (Exception e) {
      log.error("redis rate limit  error.....", e);
    }
    // }
    // }, 20, 1, TimeUnit.SECONDS);
  }

  /**
   * 创建一个限流的 key
   *
   * @param limiterInfo 限流的类型
   */
  public void initLimitKey(LimiterInfo limiterInfo) {

    if (null == rateLimiterScript) {
      return;
    }
    String maxPermits = limiterInfo.getMaxpermits().toString();
    String rate = limiterInfo.getRate().toString();

    // 执行redis 脚本
    Long result = (Long) redisTemplate.execute(rateLimiterScript,
        ImmutableList.of(limiterInfo.fullKey()),
        "init",
        maxPermits,
        rate);

    limiterInfoMap.put(limiterInfo.cashKey(), limiterInfo);
  }

  /**
   * 创建一个限流的key
   *
   * @param type       类型
   * @param key        id
   * @param maxPermits 上限
   * @param rate       速度
   */
  public void initLimitKey(String type, String key,
      Integer maxPermits, Integer rate) {

    LimiterInfo limiterInfo = LimiterInfo.builder()
        .type(type)
        .key(key)
        .maxpermits(maxPermits)
        .rate(rate)
        .build();
    initLimitKey(limiterInfo);
    /**
     * 缓存秒杀 lua 的sha1 编码，方便在其他地方获取
     */
    cacheRateLimiterSha1();
  }

  /**
   * 获取 redis lua 脚本的 sha1 编码,并缓存到 redis
   */
  public String cacheRateLimiterSha1() {
    String sha1 = rateLimiterScript.getSha1();
    redisTemplate.opsForValue().set(RATE_LIMITER_LUA_SHA_1, sha1);
    return sha1;
  }

  /**
   * 限流器的信息
   */
  public static class LimiterInfo {
    /**
     * 限流器的key，如：秒杀的id
     */
    private String key;

    /**
     * 限流器的类型，如：seckill
     */
    private String type = "default";

    /**
     * 限流器的最大桶容量
     */
    private Integer maxpermits;
    /**
     * 限流器的速率
     */
    private Integer rate;

    /**
     * 限流器的redis key
     */
    public String fullKey() {
      return RATE_LIMITER_KEY_PREFIX + type + ":" + key;
    }

    /**
     * 限流器的在map中的缓存key
     */
    public String cashKey() {
      return type + ":" + key;
    }

    // Getters and Setters
    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public Integer getMaxpermits() {
      return maxpermits;
    }

    public void setMaxpermits(Integer maxpermits) {
      this.maxpermits = maxpermits;
    }

    public Integer getRate() {
      return rate;
    }

    public void setRate(Integer rate) {
      this.rate = rate;
    }

    @Override
    public String toString() {
      return "LimiterInfo(key=" + this.getKey() + ", type=" + this.getType() + ", maxpermits=" + this.getMaxpermits()
          + ", rate=" + this.getRate() + ")";
    }

    public LimiterInfo() {
    }

    public static LimiterInfoBuilder builder() {
      return new LimiterInfoBuilder();
    }

    // Builder class
    public static class LimiterInfoBuilder {
      private String key;
      private String type;
      private Integer maxpermits;
      private Integer rate;

      public LimiterInfoBuilder key(String key) {
        this.key = key;
        return this;
      }

      public LimiterInfoBuilder type(String type) {
        this.type = type;
        return this;
      }

      public LimiterInfoBuilder maxpermits(int maxpermits) {
        this.maxpermits = maxpermits;
        return this;
      }

      public LimiterInfoBuilder rate(int rate) {
        this.rate = rate;
        return this;
      }

      public LimiterInfo build() {
        LimiterInfo limiterInfo = new LimiterInfo();
        limiterInfo.setKey(this.key);
        limiterInfo.setType(this.type);
        limiterInfo.setMaxpermits(this.maxpermits);
        limiterInfo.setRate(this.rate);
        return limiterInfo;
      }
    }
  }
}
