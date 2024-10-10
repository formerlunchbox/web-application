package com.formerlunchbox.web.limiter.propertities;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.formerlunchbox.web.limiter.executor.RedisRateLimiter;

@ConfigurationProperties(prefix = "limiter.redis")
public class RedisRateLimitProperties {
  private List<RedisRateLimiter.LimiterInfo> limiterInfos;

  public List<RedisRateLimiter.LimiterInfo> getLimiterInfos() {
    return this.limiterInfos;
  }

  public void setLimiterInfos(List<RedisRateLimiter.LimiterInfo> limiterInfos) {
    for (RedisRateLimiter.LimiterInfo limiterInfo : limiterInfos) {
      System.out.println(limiterInfos);
    }
    this.limiterInfos = limiterInfos;
  }

}
