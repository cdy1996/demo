package com.cdy.demo.framework.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuavaLimiter {
    
    
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1);
        double acquire = rateLimiter.acquire(1000);
        System.out.println(acquire);
    }
    
    /**
     * 限流
     */
    @Test
    public void test() {
        RateLimiter limiter = RateLimiter.create(5, 10, TimeUnit.SECONDS);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        final long start = System.currentTimeMillis();
        for (int i = 2; i > 0; i--) {
            executorService.submit(() -> {
                int round = 5;
                while (round-- > 0) {
                    limiter.acquire();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("time:" + (System.currentTimeMillis() - start));
                }
                
            });
        }
        
    }
}
