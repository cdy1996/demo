package com.cdy.demo.java.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {

    private final FutureTask<String> futureTask
            = new FutureTask<String>(()->{
        Thread.sleep(1000L);
        return "123";
    });


    private final Thread thread = new Thread(futureTask);

    public void start(){thread.start();}

    public String get() throws ExecutionException, InterruptedException {
            String s = futureTask.get();
        return s;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTest test = new FutureTest();
        test.start();
        String s = test.get();
        System.out.println(s);


    }
}
