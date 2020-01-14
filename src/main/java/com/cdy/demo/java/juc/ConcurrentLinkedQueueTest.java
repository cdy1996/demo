package com.cdy.demo.java.juc;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class ConcurrentLinkedQueueTest {


    public static void main(String[] args) {

        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
    
        DelayQueue<Delayed> delayeds = new DelayQueue<>();
    
        delayeds.add(new Task());
    
    }
}

class Task implements Delayed{

    private volatile long time;
    private long sequenceNumber;
    private long period;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.currentTimeMillis(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if (other == this)// compare zero ONLY if same object
        return 0;
        if (other instanceof Task) {
            Task x = (Task)other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (sequenceNumber < x.sequenceNumber)
                return -1;
            else
                return 1;
        }
        long d = (getDelay(TimeUnit.NANOSECONDS) -
                other.getDelay(TimeUnit.NANOSECONDS));
        return (d == 0)?0:((d<0)?-1:1);
    }
}
