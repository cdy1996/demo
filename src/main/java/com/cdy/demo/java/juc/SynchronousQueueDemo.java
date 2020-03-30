package com.cdy.demo.java.juc;

import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueDemo {


    public static void main(String[] args) throws IOException {
        SynchronousQueue<String> nofair = new SynchronousQueue<>(false);
        SynchronousQueue<String> fair = new SynchronousQueue<>(true);

        new Thread(() -> {
            int i = 1;
//            while (true) {
                int i1 = i++;
                System.out.println("put-1"+i1);
                try {
                    nofair.put(String.valueOf(i1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }, "put-1").start();
        new Thread(() -> {
            int i = 1;
//            while (true) {
            int i1 = i++;
            System.out.println("put-2"+i1);
            try {
                nofair.put(String.valueOf(i1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            }
        }, "put-2").start();
        new Thread(() -> {
//            while (true) {
                String poll = null;
                try {
                    poll = nofair.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("take-1"+poll);
//            }
        }, "take-1").start();
        new Thread(() -> {
//            while (true) {
                String poll = null;
                try {
                    poll = nofair.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("take-2"+poll);
//            }
        }, "take-2").start();


        System.in.read();
    }
}
