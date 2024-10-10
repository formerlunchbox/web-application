package com.formerlunchbox.web.limiter.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.formerlunchbox.web.enums.LimiterPolicy;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limiter {
  LimiterPolicy policy() default LimiterPolicy.COUNTER_LIMITER;

  /**
   * 未实现
   * 
   * @return
   */
  String ip() default "";// ip限流

  int type() default 0;// 限流类型 0单体 1集群

}
