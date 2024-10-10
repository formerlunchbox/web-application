package com.formerlunchbox.web.limiter.executor;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.limiter.LimiterAbstract;

@Component
@Scope("singleton")
public class TokenBucketLimiter implements LimiterAbstract {
  private long lastTime = 0;
  private int capacity = 2;
  private int rate = 2;
  private int interval = 1000;
  private AtomicInteger tokens = new AtomicInteger(0);
  private int applyCount = 1;// 申请令牌数

  @Override
  public synchronized boolean tryAcquire() {
    long now = System.currentTimeMillis();
    long gap = now - lastTime;

    // 与上一请求在同一时间区间
    if (lastTime != 0 && gap < interval) {
      if (tokens.get() < applyCount) {
        return true;
      } else {
        tokens.addAndGet(-applyCount);
        return false;
      }
    }

    if (lastTime == 0) {
      gap = interval;
    }
    // 时间段内的令牌生成数
    int reversePermits = (int) (gap / interval) * rate;
    int allPermits = tokens.get() + reversePermits;
    tokens.set(Math.min(allPermits, capacity));

    lastTime = now;
    if (tokens.get() < applyCount) {
      return true;
    } else {
      tokens.addAndGet(-applyCount);
      return false;
    }
  }

}
