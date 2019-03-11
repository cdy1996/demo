package com.cdy.demo.tool;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class GuavaTest {


    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(1, 10 , TimeUnit.SECONDS);
        double acquire = rateLimiter.acquire(1000);
        System.out.println(acquire);
    }
}
