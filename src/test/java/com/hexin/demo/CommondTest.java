package com.hexin.demo;

import com.netflix.hystrix.*;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CommondTest {


    public static void main(String[] args) throws InterruptedException {
//        Person person = new Person();
//        ElectricLight dd = new ElectricLight();
//        person.open(dd);
//        person.close(dd);
//
//
//        Commond on = new LightOnCommond(dd);
//        Commond off = new LightOffCommond(dd);
//        person.action(on);
//        person.action(off);

//        testHystrix();

        testRxjava();
        Thread.sleep(Integer.MAX_VALUE);
    }


    public static void testHystrix() {
        //http://blog.51cto.com/snowtiger/2059691
        //https://www.cnblogs.com/cowboys/p/7655829.html
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("group");
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("group");
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("service");

        HystrixCommandProperties.Setter commandPropertiesDefaults = HystrixCommandProperties.Setter()
                .withExecutionTimeoutInMilliseconds(100)
                .withCircuitBreakerForceOpen(true);

        HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults = HystrixThreadPoolProperties.Setter()
                .withCoreSize(10)
                .withQueueSizeRejectionThreshold(10);

        HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andCommandPropertiesDefaults(commandPropertiesDefaults)
                .andThreadPoolPropertiesDefaults(threadPoolPropertiesDefaults);

        HystrixCommand<String> command = new HystrixCommand<String>(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")) {
            @Override
            protected String run() throws Exception {
                return "hello  " + Thread.currentThread().getName();
            }
        };
        String result = command.execute();
        System.out.println(result);


        HystrixObservableCommand<String> command1 = new HystrixObservableCommand<String>(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")) {

            @Override
            protected Observable<String> construct() {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> observer) {
                        try {
                            System.out.println("in call of construct! thread:" + Thread.currentThread().getName());
                            if (!observer.isUnsubscribed()) {
//                      observer.onError(getExecutionException());  // 直接抛异常退出，不会往下执行
                                observer.onNext("Hello1" + " thread:" + Thread.currentThread().getName());
                                observer.onNext("Hello2" + " thread:" + Thread.currentThread().getName());
                                observer.onNext(" thread:" + Thread.currentThread().getName());
                                System.out.println("complete before------" + " thread:" + Thread.currentThread().getName());
                                observer.onCompleted(); // 不会往下执行observer的任何方法
                                System.out.println("complete after------" + " thread:" + Thread.currentThread().getName());
                                observer.onCompleted(); // 不会执行到
                                observer.onNext("Abc"); // 不会执行到
                            }
                        } catch (Exception e) {
                            observer.onError(e);
                        }
                    }
                });
            }
        };
        Observable<String> observable = command1.observe();
        observable.subscribe(new Subscriber<String>() {
            public void onCompleted() {
                System.out.println("completed");
            }

            public void onError(Throwable throwable) {
                System.out.println("error-----------" + throwable);
            }

            public void onNext(String v) {
                System.out.println("next------------" + v);
            }
        });


    }


    public static void testRxjava() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    System.out.println("in call of construct! thread:" + Thread.currentThread().getName());
                    if (!observer.isUnsubscribed()) {
//                      observer.onError(getExecutionException());  // 直接抛异常退出，不会往下执行
                        observer.onNext("Hello1" + " thread:" + Thread.currentThread().getName());
                        observer.onNext("Hello2" + " thread:" + Thread.currentThread().getName());
                        observer.onNext(" thread:" + Thread.currentThread().getName());
                        System.out.println("complete before------" + " thread:" + Thread.currentThread().getName());
                        observer.onCompleted(); // 不会往下执行observer的任何方法
                        System.out.println("complete after------" + " thread:" + Thread.currentThread().getName());
                        observer.onCompleted(); // 不会执行到
                        observer.onNext("Abc"); // 不会执行到
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        })
                .map(e -> {
                    System.out.println("map1"+e);
                    System.out.println(Thread.currentThread().getName());
                    return e;
                })
                .subscribeOn(Schedulers.newThread())
                .map(e -> {
                    System.out.println("map1"+e);
                    System.out.println(Thread.currentThread().getName());
                    return e;
                })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    public void onCompleted() {
                        /*System.out.println("completed");*/
                    }

                    public void onError(Throwable throwable) {
                        /*System.out.println("error-----------" + throwable);*/
                    }

                    public void onNext(String v) {
                        /*System.out.println("next------------" + v);*/
                    }
                });
    }


}


interface Commond {
    void execute();
}

class LightOnCommond implements Commond {

    ElectricLight light;

    public LightOnCommond(ElectricLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.open();
    }
}

class LightOffCommond implements Commond {

    ElectricLight light;

    public LightOffCommond(ElectricLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.close();
    }
}

class Person {
    public void open(ElectricLight dd) {
        dd.status = true;
    }

    public void close(ElectricLight dd) {
        dd.status = false;
    }

    public void action(Commond commond) {
        commond.execute();
    }

}

class ElectricLight {
    boolean status = false;

    public void open() {
        status = true;
    }

    public void close() {
        status = false;
    }

}

