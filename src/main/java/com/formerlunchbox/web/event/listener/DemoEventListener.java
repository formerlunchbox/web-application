package com.formerlunchbox.web.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.formerlunchbox.web.event.DemoEvent;

@Component
public class DemoEventListener implements ApplicationListener<DemoEvent> {

  private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DemoEventListener.class);

  @Override
  public void onApplicationEvent(DemoEvent event) {
    log.info("DemoEventListener onApplicationEvent");
  }

}
