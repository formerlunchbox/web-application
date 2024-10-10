package com.formerlunchbox.web.limiter.executor;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.limiter.LimiterAbstract;

@Component
@Scope("singleton")
public class RedissionLimiter implements LimiterAbstract {
  private final RedissonClient redissonClient;

  public RedissionLimiter(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Override
  public boolean tryAcquire() {
    // TODO Auto-generated method stub
    return LimiterAbstract.super.tryAcquire();
  }

  @Override
  public Boolean tryAcquire(String cacheKey) {
    // TODO Auto-generated method stub
    return LimiterAbstract.super.tryAcquire(cacheKey);
  }

}
