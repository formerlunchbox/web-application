package com.formerlunchbox.web.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//springboot中添加servletComponentScan注解，扫描servlet、filter、listener
//指定过滤的路径
//设置过滤器的执行顺序
@WebFilter(urlPatterns = "/*", filterName = "demoFilter")
@Order(1)
public class DemoFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(DemoFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    log.info("DemoFilter do filter");
    // // 将 ServletRequest 转换为 HttpServletRequest
    // HttpServletRequest httpRequest = (HttpServletRequest) request;

    // // 获取请求参数
    // String param = httpRequest.getParameter("paramName");
    // log.info("Request parameter 'paramName': {}", param);

    // // 获取请求头
    // String header = httpRequest.getHeader("headerName");
    // log.info("Request header 'headerName': {}", header);

    // // 获取请求的 IP 地址
    // String ipAddress = httpRequest.getRemoteAddr();
    // log.info("Request IP address: {}", ipAddress);

    // // 获取请求的 URI
    // String requestURI = httpRequest.getRequestURI();
    // log.info("Request URI: {}", requestURI);

    // // 设置响应内容类型
    // response.setContentType("text/html");
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    log.info("DemoFilter destroy");
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    /**
     * 获取初始化参数：可以通过 filterConfig.getInitParameter("paramName") 获取初始化参数。
     * 获取 Servlet 上下文：可以通过 filterConfig.getServletContext() 获取 ServletContext 对象。
     */
    log.info("DemoFilter init with config: {}", filterConfig);
  }

}
