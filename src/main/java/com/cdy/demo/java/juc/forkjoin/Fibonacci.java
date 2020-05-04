package com.cdy.demo.java.juc.forkjoin;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 斐波那契数列
 * 一个数是它前面两个数之和
 * 1,1,2,3,5,8,13,21
 */
public class Fibonacci {
    private final BigInteger RESERVED = BigInteger.valueOf(-1000);
    
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Fibonacci fib = new Fibonacci();
        int result = fib.f(1_000).bitCount();
        time = System.currentTimeMillis() - time;
        System.out.println("result，本文由公从号“彤哥读源码”原创 = " + result);
        System.out.println("test1_000() time = " + time);
    }
    
    public BigInteger f(int n) {
        Map<Integer, BigInteger> cache = new ConcurrentHashMap<>();
        cache.put(0, BigInteger.ZERO);
        cache.put(1, BigInteger.ONE);
        return f(n, cache);
    }
    
    public BigInteger f(int n, Map<Integer, BigInteger> cache) {
        BigInteger result = cache.putIfAbsent(n, RESERVED);
        if (result == null) {
            int half = (n + 1) / 2;
            RecursiveTask<BigInteger> f0_task = new RecursiveTask<BigInteger>() {
                @Override
                protected BigInteger compute() {
                    return f(half - 1, cache);
                }
            };
            f0_task.fork();
            BigInteger f1 = f(half, cache);
            BigInteger f0 = f0_task.join();
            long time = n > 10_000 ? System.currentTimeMillis() : 0;
            try {
                if (n % 2 == 1) {
                    result = f0.multiply(f0).add(f1.multiply(f1));
                } else {
                    result = f0.shiftLeft(1).add(f1).multiply(f1);
                }
                synchronized (RESERVED) {
                    cache.put(n, result);
                    RESERVED.notifyAll();
                }
            } finally {
                time = n > 10_000 ? System.currentTimeMillis() - time : 0;
                if (time > 50)
                    System.out.printf("f(%d) took %d%n", n, time);
            }
        } else if (result == RESERVED) {
            try {
                ReservedFibonacciBlocker blocker = new ReservedFibonacciBlocker(n, cache);
                ForkJoinPool.managedBlock(blocker);
                result = blocker.result;
            } catch (InterruptedException e) {
                throw new CancellationException("interrupted");
            }
        }
        return result;
        // return f(n - 1).add(f(n - 2));
    }
    
    private class ReservedFibonacciBlocker implements ForkJoinPool.ManagedBlocker {
        private final int n;
        private final Map<Integer, BigInteger> cache;
        private BigInteger result;
        
        public ReservedFibonacciBlocker(int n, Map<Integer, BigInteger> cache) {
            this.n = n;
            this.cache = cache;
        }
        
        @Override
        public boolean block() throws InterruptedException {
            synchronized (RESERVED) {
                while (!isReleasable()) {
                    RESERVED.wait();
                }
            }
            return true;
        }
        
        @Override
        public boolean isReleasable() {
            return (result = cache.get(n)) != RESERVED;
        }
    }
}