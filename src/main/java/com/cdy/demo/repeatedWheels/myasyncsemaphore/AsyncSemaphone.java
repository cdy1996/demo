package com.cdy.demo.repeatedWheels.myasyncsemaphore;

import java.util.concurrent.*;

public class AsyncSemaphone implements Runnable {
    private final ExecutorService semaphoneExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService taskExecutor;

    private final Semaphore semaphore;

    public final BlockingQueue<AsyncSemaphoneTask<?>> queue;

    public AsyncSemaphone(int num) {
        this(num, Integer.MAX_VALUE);
    }

    public AsyncSemaphone(int num, int queueNum) {
        this(num, queueNum, queueNum);
    }

    public AsyncSemaphone(int num, int queueNum, int threadNum) {
        semaphore = new Semaphore(num);
        queue = new ArrayBlockingQueue<>(queueNum);
        taskExecutor = Executors.newFixedThreadPool(threadNum);
        semaphoneExecutor.submit(this);
    }

    public void submit(AsyncSemaphoneTask<?> task) {
        queue.add(task);
    }


    @Override
    public void run() {
        while (true) {
            try {
                AsyncSemaphoneTask<?> take = queue.take();
                semaphore.acquire();

                taskExecutor.submit(() -> {
                    try {
                        take.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
