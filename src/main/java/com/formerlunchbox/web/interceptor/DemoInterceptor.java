package com.formerlunchbox.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 实现 org.springframework.web.servlet.HandlerInterceptor接口或
 * 继承 org.springframework.web.servlet.handler.HandlerInterceptorAdapter
 */

public class DemoInterceptor implements HandlerInterceptor {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoInterceptor.class);

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    log.info("DemoInterceptor afterCompletion");
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    log.info("DemoInterceptor postHandle");
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("DemoInterceptor preHandle");
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

}
