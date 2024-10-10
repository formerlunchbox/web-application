package com.formerlunchbox.web.limiter.executor;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.limiter.LimiterAbstract;

@Component
@Scope("singleton")
public class CounterLimiter implements LimiterAbstract {
  private long lastTime = System.currentTimeMillis();// 区间开始
  private long interval = 1000;// 区间长度
  private long capacity = 2;// 区间容量

  private AtomicLong accumulator = new AtomicLong(0);// 访问数

  @Override
  public boolean tryAcquire() {
    long nowTime = System.currentTimeMillis();
    if (nowTime < lastTime + interval) {
      long count = accumulator.incrementAndGet();
      return count > capacity;
    } else {
      // 在时间区间之外
      synchronized (this) {
        if (nowTime > lastTime + interval) {
          // reset Counter
          accumulator.set(1);
          lastTime = nowTime;
          return false;
        } else { // 双重检查
          long count = accumulator.incrementAndGet();
          return count > capacity; // 容量耗尽
        }
      }
    }
  }
}
