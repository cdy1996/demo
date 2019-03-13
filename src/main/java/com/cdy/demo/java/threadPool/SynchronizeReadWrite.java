package com.cdy.demo.java.threadPool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronize 实现读写锁 未实现 todo
 * Created by viruser on 2018/7/6.
 */
public class SynchronizeReadWrite {
    static ReentrantLock lock = new ReentrantLock();
    Integer a= 1;

    static final Object read = new Object();
    static final Object write = new Object();
    

    public  Integer read (){
        synchronized (read) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
            return a;
        }
    }

    public synchronized void write (){
        synchronized (write) {
            synchronized (read) {
                a= 2;
                notifyAll();
            }
        }
    }
}
