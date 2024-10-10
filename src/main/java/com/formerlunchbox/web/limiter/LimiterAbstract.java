package com.formerlunchbox.web.limiter;

public interface LimiterAbstract {

  /**
   * 尝试获取许可
   * true 限流
   * false 未限流
   * 
   * @return
   */
  default boolean tryAcquire() {
    return true;
  }

  default Boolean tryAcquire(String cacheKey) {
    return true;
  }

}
