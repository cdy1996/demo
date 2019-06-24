package com.cdy.demo.java.juc;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by viruser on 2018/6/1.
 */
public class ForkJoinWork extends RecursiveTask<Long> {
    private int start;//事件
    public static final int critical = 1;

    public ForkJoinWork(int start) {
        this.start = start;
    }



    @Override
    protected Long compute() {
        System.out.println(Thread.currentThread().getName());
        //判断是否是拆分完毕
        int lenth = start;
        if (lenth <= critical) {
            //如果拆分完毕就相加
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
            return 1L;
        } else {
            //没有拆分完毕就开始拆分
            start = start-1;//计算的两个值的中间值
            ForkJoinWork right = new ForkJoinWork(1);
            right.fork();//拆分，并压入线程队列
            ForkJoinWork left = new ForkJoinWork(start);
            left.fork();//拆分，并压入线程队列
//            invokeAll(right, left);

            //合并
            return right.join() + left.join();
        }
    }

    public static  void test() throws InterruptedException, ExecutionException, IOException {
//        ForkJoinWork forkJoinWork = new ForkJoinWork(1L, 1000000L);
        long start = System.currentTimeMillis();
//        Long compute = forkJoinWork.compute();

        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        // 提交可分解的PrintTask任务
//        ForkJoinTask<Long> submit = forkJoinPool.submit(new ForkJoinWork(4));
//        Long aLong = submit.get();
        Long invoke = forkJoinPool.invoke(new ForkJoinWork(4));
        forkJoinPool.execute(()->{
            System.out.println(Thread.currentThread().getName());
            System.out.println("!23");
        });
//        forkJoinPool.awaitTermination(2, TimeUnit.SECONDS);//阻塞当前线程直到 ForkJoinPool 中所有的任务都执行结束
        // 关闭线程池
        System.out.println(System.currentTimeMillis()-start +"==>"+invoke);
        System.in.read();
    }


    public static void main(String[] args) throws Exception {
//        ForkJoinPool executorService = (ForkJoinPool) Executors.newWorkStealingPool(10);

//        executorService.submit(()->
//                IntStream.range(1, 1000).parallel().forEach(e -> System.out.println(e + "->" + Thread.currentThread().getName()));
//        );

        test();


        System.in.read();
    }
}
