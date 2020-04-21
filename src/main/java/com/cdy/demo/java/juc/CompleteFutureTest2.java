package com.cdy.demo.java.juc;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * 测试  CompleteFuture completion之间的关系
 * tryFire 用于执行 completion执行任务, 然后会调用dep.postFire(src)
 * <p>
 * postFire  如果是mode<0 , 调用src的 cleanStack,如果自己的stack!=null 就返回 自己(d), 不然返回null;
 * 如果mode>=0 则调用src.postComplete或者自己(d)的postComplete
 * <p>
 * postComplete 用于将当前completableFuture的stack一个个取出, 然后调用 tryFire进行回调处理执行
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
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 2;
        }, Executors.newSingleThreadExecutor());

        CompletableFuture<Void> voidCompletableFuture = a.thenAccept(e -> {
        });
//        a.whenComplete();

        CompletableFuture<Void> d = a.acceptEither(b, System.out::println);
        countDownLatch.countDown();
        System.in.read();


    }

    public static void testDubbo() throws IOException {
        CompletableFuture<String> res = sendAsyncByDubbo("123");
        res.whenComplete((s, e) -> {
            System.out.println(s);
        });
        System.in.read();


    }

    private static CompletableFuture<String> sendAsyncByDubbo(String s) {
        // 这里模拟 netty 异步塞入消息
        CompletableFuture<String> completableFuture = new CompletableFuture();
        completableFuture.complete(s + "456");
        return completableFuture;
    }


}
