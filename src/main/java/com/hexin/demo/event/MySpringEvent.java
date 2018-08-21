package com.hexin.demo.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by viruser on 2018/7/21.
 */
public class MySpringEvent extends ApplicationEvent {


    String message;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MySpringEvent(Object source) {
        super(source);
    }

    public MySpringEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
