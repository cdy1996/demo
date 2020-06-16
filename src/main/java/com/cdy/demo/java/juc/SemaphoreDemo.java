package com.cdy.demo.java.juc;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {


    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(0);
        Thread thread = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
        });

        thread.start();

        semaphore.release();


    }

}

