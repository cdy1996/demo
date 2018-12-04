package com.hexin.demo.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {


    public static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);


    public static void main(String[] args) {

        ScheduledFuture<?> scheduledFuture =
                schedule.scheduleAtFixedRate(() -> System.out.println(123), 1, 10, TimeUnit.SECONDS);

//        scheduledFuture.get(, )
        scheduledFuture.getDelay(TimeUnit.SECONDS);

    }
}
