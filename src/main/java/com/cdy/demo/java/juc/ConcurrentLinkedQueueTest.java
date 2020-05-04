package com.cdy.demo.java.juc;

import java.util.concurrent.ConcurrentLinkedQueue;


public class ConcurrentLinkedQueueTest {
    
    public static void main(String[] args) {
        
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
    }
}
