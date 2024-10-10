package com.formerlunchbox.web.enums;

public enum LimiterPolicy {
  COUNTER_LIMITER, // 计数器
  LEAK_BUCKET_LIMITER, // 漏桶
  TOKEN_BUCKET_LIMITER; // 令牌桶
}
