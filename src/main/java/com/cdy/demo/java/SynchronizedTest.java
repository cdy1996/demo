package com.cdy.demo.java;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Synchronization 在抢到锁后如果将锁对象修改 则会立刻失去锁
 */
public class SynchronizedTest implements Runnable{

    public Integer i = new Integer(0);

    public static CountDownLatch count = new CountDownLatch(2);
    
    
    @Override
    public void run() {

        synchronized (i) {
            System.out.println(Thread.currentThread().getName()+"抢到了锁");
            i++;
            System.out.println(Thread.currentThread().getName()+"修改了锁对象");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"释放锁");
        }


    }


    @Test
    public void main() throws IOException {
        SynchronizedTest syunchronizedTest = new SynchronizedTest();

        Thread thread = new Thread(syunchronizedTest);

//        count.countDown();
        Thread thread1 = new Thread(syunchronizedTest);
        Thread thread2 = new Thread(syunchronizedTest);
//        count.countDown();

        thread.start();
        thread1.start();
        thread2.start();

        System.in.read();

    }
}
