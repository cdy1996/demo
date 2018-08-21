package com.hexin.demo.event;

/**
 * Created by viruser on 2018/6/29.
 */
public class ObserverTest {


    public static void main(String[] args) {
        MyObservable myObservable = new MyObservable();

        MyObserver myObserver = new MyObserver();

        myObservable.addObserver(myObserver);

        myObservable.notifyObservers("hello");
    }
}
