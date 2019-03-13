package com.cdy.demo.java.threadPool;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

public class JUCTest {
    

    @Test
    public void testPhaser() throws IOException {
        Phaser phaser =new Phaser(2);
        
        new Thread(()->{
            try {

                System.out.println("同学1 到场");
                phaser.arriveAndAwaitAdvance();
                System.out.println("同学1 开始做第一道题");
                Thread.sleep(1000L);
                System.out.println("同学1 做完第一道题");
                phaser.arriveAndDeregister();
                System.out.println("同学1 开始做第二道题");
                Thread.sleep(2000L);
                System.out.println("同学1 做完第二道题");
                System.out.println("同学1 开始做第三道题");
                Thread.sleep(1000L);
                System.out.println("同学1 做完第三道题");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(()->{
            try {
                System.out.println("同学2 到场");
                phaser.arriveAndAwaitAdvance();
                System.out.println("同学2 开始做第一道题");
                Thread.sleep(2000L);
                System.out.println("同学2 做完第一道题");
                phaser.arriveAndDeregister();
                System.out.println("同学2 开始做第二道题");
                Thread.sleep(1000L);
                System.out.println("同学2 做完第二道题");
                System.out.println("同学2 开始做第三道题");
                Thread.sleep(2000L);
                System.out.println("同学2 做完第三道题");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.in.read();
    }

    @Test
    public void testCyclicBarrier() throws IOException {
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        CountDownLatch cyclicBarrier = new CountDownLatch(1);

        new Thread(()->{
                try {
                    cyclicBarrier.await();
                    System.out.println("同学1 开始做第一道题");
                    Thread.sleep(1000L);
                    System.out.println("同学1 做完第一道题");
                /*  cyclicBarrier.await();
                    System.out.println("同学1 开始做第二道题");
                    Thread.sleep(2000L);
                    System.out.println("同学1 做完第二道题");*/
                     /* cyclicBarrier.await();
                    System.out.println("同学1 开始做第三道题");
                    Thread.sleep(1000L);
                    System.out.println("同学1 做完第三道题");*/

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();
        new Thread(()->{
                try {
                    cyclicBarrier.await();
                    System.out.println("同学2 开始做第一道题");
                    Thread.sleep(2000L);
                    /*System.out.println("同学2 做完第一道题");
                    cyclicBarrier.await();
                    System.out.println("同学2 开始做第二道题");
                    Thread.sleep(1000L);
                    System.out.println("同学2 做完第二道题");
                    cyclicBarrier.await();
                    System.out.println("同学2 开始做第三道题");
                    Thread.sleep(2000L);
                    System.out.println("同学2 做完第三道题");*/

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();

        cyclicBarrier.countDown();
        cyclicBarrier.countDown();
        cyclicBarrier.countDown();
        System.in.read();

    }

    @Test
    public void testSemaphore(){
        Semaphore semaphore = new Semaphore(1);
        new Thread(()->{
            while(true) {
                try {
                    semaphore.acquire();
                    System.out.println("111");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            while(true) {
                try {
                    semaphore.acquire();
                    System.out.println("222");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    
   
}
