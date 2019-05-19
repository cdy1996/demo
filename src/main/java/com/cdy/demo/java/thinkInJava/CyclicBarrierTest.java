package com.cdy.demo.java.thinkInJava;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * todo
 * Created by 陈东一
 * 2019/4/12 0012 22:25
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
        for (int i = 0; i < 20; i++) {
            executorService.execute(()->{
                try {
                    cyclicBarrier.await();
                    System.out.println("完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
    
    
            });
        }
    
        executorService.shutdown();
    }
}
