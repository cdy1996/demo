package com.cdy.demo.gof23Design.observer.observable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by viruser on 2018/6/29.
 */
public class MyObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println(Thread.currentThread().getName());
        System.out.println(o.hasChanged());
        System.out.println(arg);
    }
}
