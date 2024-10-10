package com.formerlunchbox.web.enums;

public enum StatusEnum {
  SUCCESS(200, "请求处理成功"),
  UNAUTHORIZED(401, "用户认证失败"),
  FORBIDDEN(403, "权限不足"),
  SERVICE_ERROR(500, "服务器去旅行了，请稍后重试"),
  PARAM_INVALID(1000, "无效的参数"), TOO_MANY_REQUESTS(1001, "请求过于频繁"),
  ;

  public final Integer code;

  public final String message;

  StatusEnum(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

}
