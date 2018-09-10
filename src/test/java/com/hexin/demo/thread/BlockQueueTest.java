package com.hexin.demo.thread;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class BlockQueueTest {
    static BlockingQueue<String> queue = new SynchronousQueue<>();

    static volatile String s;

    public static void main(String[] args) throws IOException {
        BlockQueueTest blockQueueTest = new BlockQueueTest();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("准备放入1");
                    synchronized (blockQueueTest) {
                        while (s != null) {
                            blockQueueTest.wait();
                        }
                        s = "1";
                        System.out.println("放入1");
                        blockQueueTest.notifyAll();
                    }
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("准备放入2");
                    synchronized (blockQueueTest) {
                        while (s != null) {
                            blockQueueTest.wait();
                        }
                        s = "2";
                        System.out.println("放入2");
                        blockQueueTest.notifyAll();
                    }
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (blockQueueTest) {
                        while (s == null) {
                            blockQueueTest.wait();
                        }
                        System.out.println(s);
                        s = null;
                        blockQueueTest.notifyAll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        System.in.read();
    }

    public void synchronizeQueue() throws IOException {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("准备放入1");
                    queue.put("1");
                    System.out.println("放入1");
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("准备放入2");
                    queue.put("2");
                    System.out.println("放入2");
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
        new Thread(() -> {
            while (true) {
                try {
                    String take = queue.take();
                    System.out.println(take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        System.in.read();
    }
}
