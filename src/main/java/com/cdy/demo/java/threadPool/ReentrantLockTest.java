package com.cdy.demo.java.threadPool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by viruser on 2018/7/6.
 */
public class ReentrantLockTest {
    static ReentrantLock lock = new ReentrantLock();
    Integer a= 1;

    static final Object read = new Object();
    static final Object write = new Object();


    public static void main(String[] args) {


      /*  new Thread(()->{
            try {
                lock.tryLock();
                System.out.println("thread拿到锁了");
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                lock.unlock();
            }

        }).start();
*/


        try {
            lock.lock();
            System.out.println("main拿到锁了");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            lock.unlock();
        }


    }


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
