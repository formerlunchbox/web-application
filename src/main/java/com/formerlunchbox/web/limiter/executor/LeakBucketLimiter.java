package com.formerlunchbox.web.limiter.executor;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.limiter.LimiterAbstract;

@Component
@Scope("singleton")
public class LeakBucketLimiter implements LimiterAbstract {
  private static final Logger log = LoggerFactory.getLogger(LeakBucketLimiter.class);
  private long lastOutTime = System.currentTimeMillis();
  private long interval = 1000;
  private int leakRate = 2;
  private int capacity = 20;
  private AtomicInteger waterInBucket = new AtomicInteger(0);

  @Override
  public synchronized boolean tryAcquire() {
    if (waterInBucket.get() == 0) {
      lastOutTime = System.currentTimeMillis();
      waterInBucket.addAndGet(1);
      return false;
    }
    // 当前请求和上次请求在同一区间
    long nowTime = System.currentTimeMillis();
    if (nowTime < lastOutTime + interval) {
      // 加水并未满
      if (waterInBucket.get() < capacity) {
        waterInBucket.addAndGet(1);
        return false;
      } else {
        log.info("漏桶限流");
        return true;
      }
    }
    // 当前请求和上次请求不在同一区间
    // 桶里有水
    // 计算漏水
    int waterLeak = ((int) ((System.currentTimeMillis() - lastOutTime) / 1000) * leakRate);
    int waterLeft = this.waterInBucket.get() - waterLeak;

    waterLeft = Math.max(0, waterLeft);
    waterInBucket.set(waterLeft);
    // 更新桶的时间
    lastOutTime = System.currentTimeMillis();
    // 尝试加水
    if ((waterInBucket.get()) < capacity) {
      waterInBucket.addAndGet(1);
      return false;
    } else {
      log.info("漏桶限流");
      return true;
    }
  }

}
