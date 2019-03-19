package com.cdy.demo.framework.guava;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class GuavaLimiter {
    
    
    /**
     * 限流
     */
    @Test
    public void test() {
        RateLimiter rateLimiter = RateLimiter.create(1, 10 , TimeUnit.SECONDS);
        double acquire = rateLimiter.acquire(1000);
        System.out.println(acquire);
    }
}
