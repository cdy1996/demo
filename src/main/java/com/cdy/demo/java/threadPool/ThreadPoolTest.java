package com.cdy.demo.java.threadPool;

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
        @Override
        public void execute(Runnable command) {
            super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
        }

        private Exception clientTrace(){
            return new Exception("client stack trace");
        }

        private Runnable wrap(final Runnable task, final Exception clientStack, String clientThreadName){
            return ()->{
                try {
                    task.run();
                } catch (Exception e){
                    clientStack.printStackTrace();
                    throw e;
                }
            };
        }

        /*@Override
    protected void afterExecute(Runnable r, Throwable t) {
        if(t == null && r instanceof Future<?>){
            try {

                Future<?> r1 = (Future<?>) r;
                if (r1.isDone()) {
                    r1.get();
                }
            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
            Thread.currentThread().interrupt();
        }
        if(t != null){
            log.error(t.getMessage(), t);
        }


    }*/
    };

    public static void main(String[] args) throws IOException {
        log.info("请求进来");

        poolExecutor.submit(ThreadPoolTest::execute);

//        int i = 1 / 0;

     /*   for (int i = 0; i <10; i++) {
            int finalI = i;
            poolExecutor.execute(new MyTask());
            ((ThreadPoolExecutor)poolExecutor).getActiveCount();
        }*/


/*        poolExecutor.shutdown();
        try {
            poolExecutor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                }
                System.out.println("1111111");
            });
        } catch (Exception e) {
        }
        try {
            if(poolExecutor.awaitTermination(1, TimeUnit.HOURS)){
                log.info("全部结束");

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        poolExecutor = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        try {
            poolExecutor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                }
                System.out.println("1111111");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        System.in.read();
    }

    public static void execute() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw e;
        }
        System.out.println("!23123");
    }


}
