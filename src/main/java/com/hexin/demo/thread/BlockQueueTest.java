package com.hexin.demo.thread;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class BlockQueueTest {
    static BlockingQueue<String> queue = new SynchronousQueue<>();


    public static void main(String[] args) throws IOException {
        new Thread(()->{
            try {
                System.out.println("准备放入1");
                queue.put("1");
                System.out.println("放入1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
        new Thread(()->{
            try {
                System.out.println("准备放入2");
                queue.put("2");
                System.out.println("放入2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
        new Thread(()->{
            try {
                System.out.println("准备放入3");
                queue.put("3");
                System.out.println("放入3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
        new Thread(()->{
            try {
                String take = queue.take();
                System.out.println(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
        new Thread(()->{
            try {
                String take = queue.take();
                System.out.println(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        System.in.read();
    }
}
