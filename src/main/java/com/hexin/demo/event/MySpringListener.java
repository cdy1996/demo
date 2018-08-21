package com.hexin.demo.event;

import org.springframework.context.ApplicationListener;

/**
 * Created by viruser on 2018/7/21.
 */
public class MySpringListener implements ApplicationListener<MySpringEvent> {
    @Override
    public void onApplicationEvent(MySpringEvent event) {
        System.out.println(event.getMessage());
    }
}
