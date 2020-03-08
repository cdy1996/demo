package com.cdy.demo.java.juc;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * 测试  CompleteFuture completion之间的关系
 *  以及相关的  postcomplete , tryFire, postFire方法
 *               tryFire 用于执行 completion执行任务, 然后会调用postFire
 *               postFire 用于dep 通知src已经完成会 调用 cleanStack 或者是 postComplete
 *               postComplete 用于将当前completableFuture的stack都清理, 会调用 tryFire
 * Created by 陈东一
 * 2020/3/7 0007 14:48
 */
public class CompleteFutureTest2 {
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    
    public static void main(String[] args) throws IOException {
        CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 1;
        }, Executors.newSingleThreadExecutor());
        CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        }, Executors.newSingleThreadExecutor());
    
        CompletableFuture<Void> d = a.acceptEither(b, System.out::println);
        countDownLatch.countDown();
        System.in.read();
    
    }
}
