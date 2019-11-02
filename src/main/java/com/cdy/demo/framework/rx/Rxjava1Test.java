package com.cdy.demo.framework.rx;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class Rxjava1Test {


    @Test
    public void create() throws IOException {
    
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("item1");
                subscriber.onNext("item2");
                subscriber.onNext("item3");
                subscriber.onCompleted();
            }
        });
    
        Observable<Observable<String>> window = stringObservable.window(1000, TimeUnit.MILLISECONDS);
    
        Subscription subscribe = window.subscribe(new Subscriber<Observable<String>>() {
            @Override
            public void onCompleted() {
                log.info("complete window");
            }
        
            @Override
            public void onError(Throwable e) {
            
            }
        
            @Override
            public void onNext(Observable<String> stringObservable) {
                stringObservable.subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        log.info("complete");
                    }
                
                    @Override
                    public void onError(Throwable e) {
                    
                    }
                
                    @Override
                    public void onNext(String s) {
                        log.info(s);
                    }
                });
            }
        });
        System.in.read();

        
        /*Observable<String> map = stringObservable.map(e -> {
            System.out.println("map");
            return e + "map";
        });*/
        
//        Observable<String> reduce = map.reduce((a, b) -> a + b);
        
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String fruit) {
//                        log.info(fruit);
//                    }
//                });
    
//        Observable<String> scheduleObservable = map.subscribeOn(Schedulers.from(executorService));
//        Observable<String> stringObservable1 = map.observeOn(Schedulers.from(executorService));
       /*
        Subscription end = map.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("end");
            }
        
            @Override
            public void onError(Throwable throwable) {
            
            }
        
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });*/
//        System.out.println(end);
    }

    @Test
    public void from() {
        List<String> fruitList = Arrays.asList("apple", "orange");
        Observable.from(fruitList)
                .map(e -> e + ";")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String fruit) {
                        log.info("fruit = {}", fruit);
                    }
                });
    }

    /**
     * 只有当订阅者订阅才创建Observable，
     * 为每个订阅创建一个新的Observable。内部通过OnSubscribeDefer在订阅时调用Func0创建Observable
     */
    @Test
    public void defer() {
        List<String> fruitList = Arrays.asList("apple", "orange");
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.from(fruitList);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String fruit) {
                log.info("defer fruit = {}", fruit);
            }
        });
    }


    @Test
    public void window() {
        List<Integer> list = Arrays.asList(10, 5, 3, 2, 1, 0);
        Observable.from(list)
                .window(2, 2)
                .subscribe(new Action1<Observable<Integer>>() {
            @Override
            public void call(Observable<Integer> integerObservable) {

                integerObservable.forEach(e -> {
                    System.out.println(e.toString());
                });
                /*integerObservable.reduce((sum, num) -> sum+=num).subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        log.info("我被2个打印一次 = {}" , integer);
                    }
                });*/
            }
        });
    }

    @Test
    public void window2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("我是生产者1.........");
                subscriber.onNext("我是生产者2.........");
                subscriber.onNext("我是生产者3.........");
            }
        });
        stringObservable.window(10, TimeUnit.SECONDS)
                .subscribe(new Action1<Observable<String>>() {
                    @Override
                    public void call(Observable<String> stringObservable) {
                        stringObservable.subscribe((e)->{
                            Calendar calendar = Calendar.getInstance();
                            int i = calendar.get(Calendar.SECOND);
                            log.info("我会{}就被唤醒触发...", i);
                        });
                    }
                });
        countDownLatch.await();
    }
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
                    for (;;) {
                        subscriber.onNext(new Event(new Random().nextBoolean(), System.currentTimeMillis()-start));
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
                        window.scan(new Metric(System.currentTimeMillis()-start), (a, e) -> {
//                            System.out.println("scan"+Thread.currentThread().getName());
                            System.out.println(a.id);
                            return new Metric(a.success + e[0], a.fail + e[1], a.total + e[2],  e[3], a.id);
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
class Metric{
    long success;
    long fail;
    long total;
    long end;
    long id;

    public Metric(long end) {
        this.success = 0;
        this.fail = 0;
        this.total = 0;
        Rxjava1Test.longAdder.increment();
        this.id = Rxjava1Test.longAdder.longValue();
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
class Event{
    boolean success;
    long start;

    public Event(boolean success, long start) {
        this.success = success;
        this.start = start;
    }
}