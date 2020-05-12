package com.cdy.demo.java.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:BiasedLockingStartupDelay=0 关闭延迟偏向
 * -XX:-UseCompressedOops 是否开启压缩指针
 */
public class Markword {

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(6000L);
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        System.out.println("====");

//        testBiased(o);
        testLight(o);
//        testWeight(o);

    }

    public static void testBiased(Object o) throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        });

        thread.start();
        thread.join();
    }


    public static void testLight(Object o) throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        });

        thread.start();
        thread.join();
        Thread.sleep(10L);
        thread2.start();
        thread2.join();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

    }


    public static void testWeight(Object o) throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        });

        thread2.start();
        thread.start();
        thread.join();
        thread2.join();
    }
}
