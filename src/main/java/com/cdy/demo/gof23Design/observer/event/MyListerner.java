package com.cdy.demo.gof23Design.observer.event;

import java.util.EventListener;

/**
 * Created by viruser on 2018/6/29.
 */
public class MyListerner implements EventListener {


    void change(MyEvent myEvent) {
        System.out.println(myEvent.getSource().toString());
    }
}
