package com.hexin.demo.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.*;


public class ThreadPoolTest {

    static Logger log = LoggerFactory.getLogger(ThreadPoolTest.class);

    //    static ExecutorService poolExecutor = Executors.newFixedThreadPool(2);
    static ExecutorService poolExecutor = new ThreadPoolExecutor(2, 2,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()) {

//    @Override
//    protected void afterExecute(Runnable r, Throwable t) {
//        if(t == null && r instanceof Future<?>){
//            try {
//
//                Future<?> r1 = (Future<?>) r;
//                if (r1.isDone()) {
//                    r1.get();
//                }
//            } catch (InterruptedException | ExecutionException e) {
////                e.printStackTrace();
//                log.error(e.getMessage(), e);
//            }
//            Thread.currentThread().interrupt();
//        }
//        if(t != null){
//            log.error(t.getMessage(), t);
//        }
//
//
//    }
    };

    public static void main(String[] args) throws IOException {
        log.info("请求进来");
//        int i = 1 / 0;
        poolExecutor.execute(ThreadPoolTest::execute);
//        poolExecutor.shutdownNow();


        log.info("请求返回");
        System.in.read();
    }

    public static void execute() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw e;
        }
    }


}
