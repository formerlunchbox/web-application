package com.formerlunchbox.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.formerlunchbox.web.enums.LimiterPolicy;
import com.formerlunchbox.web.event.DemoEvent;
import com.formerlunchbox.web.limiter.annotation.Limiter;
import com.formerlunchbox.web.pojo.response.ResultResponse;

@RestController
@RequestMapping("/api")
@ResponseBody
public class DoorContoller {
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @GetMapping("/door")
  @Limiter(policy = LimiterPolicy.COUNTER_LIMITER)
  public ResultResponse<String> door() {
    return ResultResponse.success("door");
  }

  @GetMapping("/event")
  public String triggerEeent() {
    eventPublisher.publishEvent(new DemoEvent(this, "i am come"));
    return "event end";
  }

}