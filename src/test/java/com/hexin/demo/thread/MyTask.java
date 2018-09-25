package com.hexin.demo.thread;

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
}
