package com.formerlunchbox.web.pojo.response;

import java.io.Serializable;

import com.formerlunchbox.web.enums.StatusEnum;

/**
 * DO数据库对应类：nameDO
 * DTO接收前端传入参数：DTO
 * VO后端响应给前端：用到的页面NameVo
 * BO后端业务逻辑处理：NameBo
 * 统称为POJO
 */

public class ResultResponse<T> implements Serializable {

  /**
   * 接口响应状态码
   */
  private Integer code;

  /**
   * 接口响应信息
   */
  private String msg;

  /**
   * 接口响应的数据
   */
  private T data;

  public Integer getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }

  public T getData() {
    return data;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public void setData(T data) {
    this.data = data;
  }

  /**
   * 封装成功响应的方法
   * 
   * @param data 响应数据
   * @return reponse
   * @param <T> 响应数据类型
   */
  public static <T> ResultResponse<T> success(T data) {

    ResultResponse<T> response = new ResultResponse<>();
    response.setData(data);
    response.setCode(StatusEnum.SUCCESS.code);
    return response;
  }

  /**
   * 封装error的响应
   * 
   * @param statusEnum error响应的状态值
   * @return
   * @param <T>
   */
  public static <T> ResultResponse<T> error(StatusEnum statusEnum) {
    return error(statusEnum, statusEnum.message);
  }

  /**
   * 封装error的响应 可自定义错误信息
   * 
   * @param statusEnum error响应的状态值
   * @return
   * @param <T>
   */
  public static <T> ResultResponse<T> error(StatusEnum statusEnum, String errorMsg) {
    ResultResponse<T> response = new ResultResponse<>();
    response.setCode(statusEnum.code);
    response.setMsg(errorMsg);
    return response;
  }

}
