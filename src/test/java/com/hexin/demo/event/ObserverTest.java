package com.hexin.demo.event;

/**
 * Created by viruser on 2018/6/29.
 */
public class ObserverTest {


    public static void main(String[] args) throws InterruptedException {
        MyObservable myObservable = new MyObservable();






        new Thread(()->{
            MyObserver myObserver = new MyObserver();
            myObservable.addObserver(myObserver);

        },"111").start();
        new Thread(()->{
            MyObserver myObserver = new MyObserver();
            myObservable.addObserver(myObserver);

        },"222").start();


        Thread.sleep(100L);
        myObservable.notifyObservers("hello");
    }
}
