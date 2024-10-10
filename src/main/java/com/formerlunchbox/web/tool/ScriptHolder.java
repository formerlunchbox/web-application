package com.formerlunchbox.web.tool;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import com.formerlunchbox.web.limiter.executor.RedisRateLimiter;

public class ScriptHolder {
  private ScriptHolder() {
    // Private constructor to hide the implicit public one
  }

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ScriptHolder.class);
  // lua 脚本的类路径
  private static String rateLimitLua = "script/rate_limiter.lua";
  private static RedisScript<Long> rateLimiterScript = null;

  public static synchronized RedisScript<Long> getRateLimitScript() {
    if (null == rateLimiterScript) {
      // 从类路径文件中，加载 lua 脚本
      // 使用RedisRateLimiter是确保在类加载器能找到脚本
      String script = IOUtil.loadJarFile(RedisRateLimiter.class.getClassLoader(), rateLimitLua);
      if (StringUtils.isEmpty(script)) {
        log.error("lua script load failed:" + rateLimitLua);

      } else {
        // 创建 lua 脚本实例
        rateLimiterScript = new DefaultRedisScript<>(script, Long.class);
      }
    }
    return rateLimiterScript;
  }

}
