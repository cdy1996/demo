package com.hexin.demo.event;

import java.util.EventObject;

/**
 * Created by viruser on 2018/6/29.
 */
public class MyEvent extends EventObject{
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MyEvent(Object source) {
        super(source);
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }
}
