package com.formerlunchbox.web.limiter.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.enums.LimiterPolicy;
import com.formerlunchbox.web.limiter.LimiterAbstract;
import com.formerlunchbox.web.limiter.annotation.Limiter;
import com.formerlunchbox.web.limiter.exceptions.RateLimitExceededException;

@Component
@Aspect
public class LimiterAop {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LimiterAop.class);
  @Autowired
  private LimiterAbstract counterLimiter;
  @Autowired
  private LimiterAbstract leakBucketLimiter;

  @Before("@annotation(com.formerlunchbox.web.limiter.annotation.Limiter)")
  public void beforeLimit(JoinPoint joinPoint, Limiter limiter) {
    LimiterPolicy policy = limiter.policy();
    logger.info("policy: {}", policy);
    switch (policy) {
      case COUNTER_LIMITER:
        if (counterLimiter.tryAcquire()) {
          throw new RateLimitExceededException("Rate limit exceeded");
        }
        break;
      case LEAK_BUCKET_LIMITER:
        if (leakBucketLimiter.tryAcquire()) {
          throw new RateLimitExceededException("Rate limit exceeded");
        }
        break;
      case TOKEN_BUCKET_LIMITER:
        System.out.println("令牌桶限流");
        break;
      default:
        break;
    }
  }
}
