package com.cdy.demo.java.threadPool;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 可重入锁测试
 * Created by 陈东一
 * 2019/3/13 0013 23:35
 */
public class ReentrantLockTest {
    
    
    @Test
    public void testReenrantLock(){
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
    
    @Test
    public void testReenrantReadWriteLock(){
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
