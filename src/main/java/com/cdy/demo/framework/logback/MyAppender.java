package com.cdy.demo.framework.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * logback 自定义appender
 * Created by 陈东一
 * 2019/3/12 0012 0:05
 */
public class MyAppender  extends AppenderBase<LoggingEvent> {
    @Override
    protected void append(LoggingEvent eventObject) {
        System.out.println(eventObject.getMessage());
    }
}
