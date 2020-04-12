package com.cdy.demo.framework.rx.rxjava1;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * todo
 * Created by 陈东一
 * 2020/4/12 0012 15:40
 */
public class windowslidedemo {
    
    static LongAdder longAdder = new LongAdder();
    
    @Test
    public void windowSlide() throws InterruptedException, IOException {
        
        long start = System.currentTimeMillis();
        
        final List<long[]> emptyEventCountsToStart = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            emptyEventCountsToStart.add(getEmptyBucketSummary());
        }
        
        Observable<Event> eventObservable = Observable.create(new Observable.OnSubscribe<Event>() {
            @Override
            public void call(Subscriber<? super Event> subscriber) {
                
                try {
                    for (; ; ) {
                        subscriber.onNext(new Event(new Random().nextBoolean(), System.currentTimeMillis() - start));
                        Thread.sleep(new Random().nextInt(50));
                    }
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Observable<long[]> observable = eventObservable.window(100, TimeUnit.MILLISECONDS)
                .flatMap(bucket ->
                                bucket.reduce(getEmptyBucketSummary(), (a, b) -> {
//                            System.out.println("reduce"+Thread.currentThread().getName());
                                    if (b.success) a[0] += 1;
                                    if (!b.success) a[1] += 1;
                                    a[2] += 1;
                                    a[3] = b.start;
                                    return a;
                                })
                ).startWith(emptyEventCountsToStart);
        
        
        // skip 1 是滑动的关键
        Observable<Metric> metricObservable = observable.window(10, 1)
                .flatMap(window ->
                                window.scan(new Metric(System.currentTimeMillis() - start), (a, e) -> {
//                            System.out.println("scan"+Thread.currentThread().getName());
                                    System.out.println(a.id);
                                    return new Metric(a.success + e[0], a.fail + e[1], a.total + e[2], e[3], a.id);
                                }).skip(10)
                ).onBackpressureDrop();


//        metricObservable.subscribe((metric)->{
//
//        });
        metricObservable.subscribe(new Subscriber<Metric>() {
            @Override
            public void onCompleted() {
            
            }
            
            @Override
            public void onError(Throwable e) {
            
            }
            
            @Override
            public void onNext(Metric metric) {
                System.out.println(metric.toString());
            }
        });
        
        
        System.in.read();
    }
    
    long[] getEmptyBucketSummary() {
        return new long[4];
    }
    
}

class Metric {
    long success;
    long fail;
    long total;
    long end;
    long id;
    
    public Metric(long end) {
        this.success = 0;
        this.fail = 0;
        this.total = 0;
        windowslidedemo.longAdder.increment();
        this.id = windowslidedemo.longAdder.longValue();
        this.end = System.currentTimeMillis();
    }
    
    public Metric(long success, long fail, long total, long end, long id) {
        this.success = success;
        this.fail = fail;
        this.total = total;
        this.end = end;
        this.id = id;
    }
    
    
    @Override
    public String toString() {
        return "Metric{" +
                "success=" + success +
                ", fail=" + fail +
                ", total=" + total +
                ", end=" + end +
                ", id=" + id +
                '}';
    }
}

class Event {
    boolean success;
    long start;
    
    public Event(boolean success, long start) {
        this.success = success;
        this.start = start;
    }
}