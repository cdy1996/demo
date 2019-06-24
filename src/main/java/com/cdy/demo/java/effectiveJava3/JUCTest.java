package com.cdy.demo.java.effectiveJava3;

import java.util.concurrent.*;

public class JUCTest {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        long time = time(executorService, 2, () ->
        {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(123);
        });
        System.out.println(time);


    }

    public static long time(Executor executor, int concurrency, Runnable action) throws Exception {
//        return workWithCountDownLatch(executor, concurrency, action);
            return workWithCyclicBarrier(executor, concurrency, action);
    }
    private static long workWithCountDownLatch(Executor executor, int concurrency, Runnable action) throws InterruptedException, BrokenBarrierException {
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);

        for (int i = 0; i < concurrency; i++) {
            executor.execute(()->{
                ready.countDown();
                try {
                    start.await(); //等待统一的开始
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await(); //等待所有线程准备完毕
        System.out.println("等待所有线程准备完毕");
        long startTime = System.nanoTime();
        start.countDown();
        done.await(); // 等待所有线程执行完毕
        System.out.println("等待所有线程执行完毕");
        return System.nanoTime()-startTime;
    }

    private static long workWithCyclicBarrier(Executor executor, int concurrency, Runnable action) throws InterruptedException, ExecutionException {

        FutureTask<Long> future = new FutureTask<>(()->{
            System.out.println("等待所有线程准备完毕");
            return System.nanoTime();
        });
        FutureTask<Long> end = new FutureTask<>(()->{
            System.out.println("等待所有线程执行完毕");
            return System.nanoTime();
        });


        CyclicBarrier ready = new CyclicBarrier(concurrency, future);
        CyclicBarrier endB = new CyclicBarrier(concurrency, end);


        for (int i = 0; i < concurrency; i++) {
            executor.execute(()->{
                try {
                    ready.await(); //统一开始
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        endB.await();//统一开始
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }



        return end.get()- future.get();
    }

    private static long workWithCyclicBarrier2(Executor executor, int concurrency, Runnable action) throws InterruptedException, ExecutionException, BrokenBarrierException {
        CyclicBarrier ready = new CyclicBarrier(concurrency+1);
        for (int i = 0; i < concurrency; i++) {
            executor.execute(()->{
                try {
                    ready.await(); //统一开始
                    action.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ready.await();//统一开始
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        ready.await();
        long start = System.nanoTime();
        ready.await();

        return System.nanoTime()- start;
    }
}
