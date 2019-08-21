package com.cdy.demo.java.juc;

import io.netty.util.concurrent.CompleteFuture;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CompleteFutureTest {


    /**
     * Thread-0111-2    2,5是先后关系,因为是通过返回值再去监听的
     * Thread-0111-5
     * Thread-0111-1    2,1,3是倒序输出的,因为是针对同一个对象的监听,会被倒序执行
     * Thread-0111-3    3,6也是先后关系,因为是通过返回值再去监听的
     * Thread-0222-6
     * Thread-1111   -4
     * Thread-1333   -7
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
//        Future<Double> priceAsync = getPriceAsync("!23");
//        System.out.println(priceAsync.get());


        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<String> first = CompletableFuture.supplyAsync(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "111";
        });


        CompletableFuture<String> third = first.thenApply((r) -> {
            System.out.println(Thread.currentThread().getName() + r + "-3");
            return "222";
        });
        first.whenComplete((r,e)->{
            System.out.println(Thread.currentThread().getName()+r+"-1");
        });
        // first.whenComplete -> first.whenComplete 这种时倒序输出的
        CompletableFuture<String> second = first.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName()+r + "-2");
        });

        // 这个永远在都输第二 是因为是异步的关系
        CompletableFuture<String> fourth = first.thenApplyAsync(r -> {
            System.out.println(Thread.currentThread().getName() + r + "   -4");
            return "333";
        });

        // first -> second 这种时顺序输出的
        second.whenComplete((r, e) -> {
             System.out.println(Thread.currentThread().getName()+r + "-5");
        });

        third.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + r + "-6");
        });
        fourth.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + r + "   -7");
        });

        countDownLatch.countDown();
        System.in.read();

    }
    
    public static Future<Double> getPriceAsync(String product) {
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception ex) {
                futurePrice.completeExceptionally(ex);
            }
        }).start();
        return futurePrice;
    }
    
    private static double calculatePrice(String product) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Test
    public void whenComplete() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(100);

            return 100;
        });
        future.whenComplete((l, r) -> {
            System.out.println(l);
            System.out.println(r);
        });
        System.in.read();
    }
    @Test
    public void thenApply() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(10000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        future.thenApply(i -> {
            return -i;
        });
        System.in.read();
    }
    @Test
    public void thenAccept() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        future.thenAccept(System.out::println);
        System.in.read();
    }
    @Test
    public void thenRun() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
//        future.thenRun(() -> System.out.println("Done"));
        future.thenRunAsync(() -> System.out.println("Done"));
        System.in.read();
    }
    @Test
    public void thenAcceptBoth() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("100");
            return 100;
        });
        CompletableFuture<Integer> other = CompletableFuture.supplyAsync(() -> {
            System.out.println("200");
            return 200;
        });
        /*
           如果该操作创建时上面两个都完成了,那么直接就可以执行comsumer
           如果上面两个操作任意一个未完成,则创建对应的CompletableFuture和Completion
           然后将Completion放到other的stack中,当other完成时通知当前的completion

         */
        future.thenAcceptBothAsync(other, (x, y) -> System.out.println(x + y));
        System.in.read();
    }
    @Test
    public void acceptEither() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Integer> other = CompletableFuture.supplyAsync(() -> 200);
        future.acceptEither(other, System.out::println);
        System.in.read();
    }
    @Test
    public void allOf() throws IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> 200);
        CompletableFuture<Integer> third = CompletableFuture.supplyAsync(() -> 300);
        CompletableFuture.allOf(future, second, third);
        System.in.read();
    }
    @Test
    public void anyOf() throws InterruptedException, ExecutionException, IOException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Integer> second = CompletableFuture.supplyAsync(() -> 200);
        CompletableFuture<Integer> third = CompletableFuture.supplyAsync(() -> 300);
        CompletableFuture.anyOf(future, second, third);
        System.in.read();
    }
}
