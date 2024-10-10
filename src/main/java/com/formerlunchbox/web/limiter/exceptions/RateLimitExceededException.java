package com.formerlunchbox.web.limiter.exceptions;

import com.formerlunchbox.web.enums.StatusEnum;

public class RateLimitExceededException extends RuntimeException {

  public RateLimitExceededException(String message) {
    super(message);
  }

  public StatusEnum getStatus() {
    return StatusEnum.TOO_MANY_REQUESTS;
  }

}
