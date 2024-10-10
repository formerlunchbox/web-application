package com.formerlunchbox.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.formerlunchbox.web.interceptor.DemoInterceptor;

@Configuration
public class IntercepConfig implements WebMvcConfigurer {

  @Bean
  public DemoInterceptor demoInterceptor() {
    return new DemoInterceptor();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 添加拦截器
    registry.addInterceptor(new DemoInterceptor()).addPathPatterns("/**");
  }

}
