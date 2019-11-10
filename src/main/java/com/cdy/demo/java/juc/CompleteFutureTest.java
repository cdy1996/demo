package com.cdy.demo.java.juc;

import io.netty.util.concurrent.CompleteFuture;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.*;

public class CompleteFutureTest {



    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException, TimeoutException {
//        Future<Double> priceAsync = getPriceAsync("!23");
//        System.out.println(priceAsync.get());

        testMultiFuture();

        System.in.read();
    }

    public static void testMultiFuture() throws InterruptedException, ExecutionException, TimeoutException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CompletableFuture<String> origin = CompletableFuture.supplyAsync(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "111";
        });



        CompletableFuture<String> first = origin.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + r + " -1");
        });
        CompletableFuture<String> second = origin.thenApply((r) -> {
            System.out.println(Thread.currentThread().getName() + r + " -2");
            return "222";
        });
        CompletableFuture<String> third = origin.thenApplyAsync(r -> {
            System.out.println(Thread.currentThread().getName() + r + " -3");
            return "111";
        });


        // origin -> second 这种时顺序输出的
        first.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName()+r + " -11");
        });


        CompletableFuture<String> ten = second.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + r + " -22");
        });
        second.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName()+r + " -222");
        });
//        third.whenComplete((r, e) -> {
//            System.out.println(Thread.currentThread().getName() + r + " -33");
//        });

        ten.whenComplete((r, e) -> {
            System.out.println(Thread.currentThread().getName() + r + " -1010");
        });

        ten.get(100, TimeUnit.SECONDS);
        countDownLatch.countDown();
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
