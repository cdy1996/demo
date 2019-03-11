package com.cdy.demo.java.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class MyTask implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 60 * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"计算完成");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ForkJoinPool pool = new ForkJoinPool(2);
        pool.submit(() -> {
            return LongStream.range(1, 50 * 1024 * 1024).boxed().collect(Collectors.toList())
                    .stream()
                    .parallel()
                    .map(x -> x * 2)
                    .filter(x -> x < 1500)
                    .reduce((x,y) -> x+y)
                    .get();
        }).get();
    }
}
