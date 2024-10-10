package com.formerlunchbox.web.redis;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestRedissonLimiter {
  @Autowired
  private RedissonClient redissonClient;

  @Test
  void testRedissonLimit() {
    String key = "redisson-limiter-key";

    // Declare a rate limiter
    RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

    // Set rate: 3 tokens every 5 seconds
    rateLimiter.trySetRate(RateType.OVERALL, 3, Duration.ofSeconds(5));
    // Try to acquire a token, should return true if successful
    boolean acquired = rateLimiter.tryAcquire(3);

    // Assert that the token was acquired
    assertTrue(acquired, "Failed to acquire token from rate limiter");
  }
}