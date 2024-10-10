package com.formerlunchbox.web.event;

import org.springframework.context.ApplicationEvent;

public class DemoEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;
  private String message;

  public DemoEvent(Object source, String message) {
    super(source);
    this.message = message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "DemoEvent [message=" + message + "]";
  }

}
