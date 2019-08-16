package com.cdy.demo.framework.netty.demo;

import io.netty.util.HashedWheelTimer;
import org.agrona.DeadlineTimerWheel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WheelTimerDemo {


    public static void main(String[] args) throws IOException {


        netty();
//        agrona();
        System.in.read();

    }

    private static void netty() {
        // 间隔100毫秒 , 一圈10个时间片
        HashedWheelTimer hashedWheelTimer = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 16);
        hashedWheelTimer.newTimeout((timeout)-> System.out.println(timeout + "!23"), 2, TimeUnit.SECONDS);
        hashedWheelTimer.start();
        while (true) {
            System.out.println(hashedWheelTimer);
        }
    }

    private static void agrona() {
        DeadlineTimerWheel deadlineTimerWheel =
                new DeadlineTimerWheel(TimeUnit.MILLISECONDS, System.currentTimeMillis(), 128, 16);
        long l = deadlineTimerWheel.scheduleTimer(100);
        System.out.println(l);
        deadlineTimerWheel.forEach((deadline,timeId)->{
            System.out.println(deadline);
            System.out.println(timeId);
        });

    }


}
