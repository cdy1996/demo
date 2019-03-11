package com.cdy.demo.gof23Design.observer.observable;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by viruser on 2018/6/29.
 */
public class MyObservable extends Observable {

    public MyObservable() {
        super();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }


    @Override
    public void notifyObservers(Object arg) {
        super.setChanged();
        super.notifyObservers(arg);
    }
}
