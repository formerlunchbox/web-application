package com.formerlunchbox.web.aop;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.formerlunchbox.web.enums.StatusEnum;
import com.formerlunchbox.web.limiter.exceptions.RateLimitExceededException;
import com.formerlunchbox.web.pojo.response.ResultResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionAdvice {
  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExceptionAdvice.class);

  @ExceptionHandler(value = RateLimitExceededException.class)
  @ResponseBody
  public ResultResponse<Void> handleControllerExceptions(
      RateLimitExceededException serviceException,
      HttpServletRequest request) {
    // log.warn("request {} throw ServiceException \n", request, serviceException);
    return ResultResponse.error(serviceException.getStatus(), serviceException.getMessage());
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResultResponse<Void> handleControllerExceptions(
      Exception serviceException,
      HttpServletRequest request) {
    // log.warn("request {} throw ServiceException \n", request, serviceException);
    return ResultResponse.error(StatusEnum.SERVICE_ERROR, serviceException.getMessage());
  }
}
