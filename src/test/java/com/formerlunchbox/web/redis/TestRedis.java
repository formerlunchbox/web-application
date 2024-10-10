package com.formerlunchbox.web.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class TestRedis {
  @Autowired
  RedisTemplate<String, String> redisTemplate;
  @Autowired
  RedissonClient redissonClient;

  @Test
  void testConnection() {
    String key = "test";
    String value = "testValue";
    redisTemplate.opsForValue().set(key, value);
    Assertions.assertEquals(value, redisTemplate.opsForValue().get(key));
  }

  @Test
  void TestRedisson() {
    String key = "test";
    String value = "testValue";
    redissonClient.getBucket(key).set(value);
    Assertions.assertEquals(value, redissonClient.getBucket(key).get());
  }
}
