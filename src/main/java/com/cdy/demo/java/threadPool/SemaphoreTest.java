package com.cdy.demo.java.threadPool;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SemaphoreTest {


    public static void main(String[] args) throws IOException {
        testReenrantReadWriteLock();
    }

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

    public static void testSemaphore(){
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

    public static void testReenrantLock(){
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(()->{
                lock.lock();
                System.out.println("111");
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        }).start();
        new Thread(()->{
                lock.lock();
                System.out.println("222");
            condition.signal();
                lock.unlock();
        }).start();

    }

    public static void testReenrantReadWriteLock(){
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock lock = reentrantReadWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        new Thread(()->{
            lock.lock();
            System.out.println("读锁1");
            lock.unlock();
        },"read1").start();
        new Thread(()->{
            lock.lock();
            System.out.println("读锁2");
            lock.unlock();
        },"read2").start();
        new Thread(()->{
            lock.lock();
            System.out.println("读锁3");
            lock.unlock();
        },"read3").start();
        new Thread(()->{
            lock.lock();
            System.out.println("读锁4");
            lock.unlock();
        },"read4").start();
        new Thread(()->{
            writeLock.lock();
            System.out.println("写锁");
            writeLock.unlock();
        },"write").start();
    }
}
