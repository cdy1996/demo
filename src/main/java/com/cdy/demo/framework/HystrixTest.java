package com.cdy.demo.framework;

import com.netflix.hystrix.*;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

import java.util.Random;
import java.util.function.Supplier;

public class HystrixTest {


    @Test
    public void test(){

    }

    @Test
    public void testHystrix() {
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("group");
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("group");
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("service");

        HystrixCommandProperties.Setter commandPropertiesDefaults = HystrixCommandProperties.Setter()
                .withCircuitBreakerEnabled(true)//默认是true，本例中为了展现该参数
                .withCircuitBreakerForceOpen(false)//默认是false，本例中为了展现该参数
                .withCircuitBreakerForceClosed(false)//默认是false，本例中为了展现该参数
                .withCircuitBreakerErrorThresholdPercentage(5)//(1)错误百分比超过5%
                .withCircuitBreakerRequestVolumeThreshold(10)//(2)10s以内调用次数10次，同时满足(1)(2)熔断器打开
                .withCircuitBreakerSleepWindowInMilliseconds(5000)//隔5s之后，熔断器会尝试半开(关闭)，重新放进来请求
                .withExecutionTimeoutInMilliseconds(500);

        HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults = HystrixThreadPoolProperties.Setter()
//                .withCoreSize(10)
                .withMaxQueueSize(10)   //配置队列大小
                .withCoreSize(2);    // 配置线程池里的线程数
//                .withQueueSizeRejectionThreshold(10);

        HystrixCommand.Setter setter = HystrixCommand.Setter.withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andCommandPropertiesDefaults(commandPropertiesDefaults)
                .andThreadPoolPropertiesDefaults(threadPoolPropertiesDefaults);

        Supplier<HystrixCommand<String>> command = ()->new HystrixCommand<String>(setter) {



            @Override
            protected String run() throws Exception {
                System.out.println("调用远程服务");

                Random rand = new Random();
                //模拟错误百分比(方式比较粗鲁但可以证明问题)
                if(1==rand.nextInt(2)){
                    throw new Exception("make exception");
                }
//                    Thread.sleep(1000L);
                return "hello  " + Thread.currentThread().getName();
            }

            @Override
            protected String getFallback() {
                return "fail";
            }
        };


        for (int i = 0; i <20; i++) {
            try {//因为配置了10秒内 至少接收到10次, 如果不睡眠就不到10秒是无效的
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            HystrixCommand<String> command1 = command.get();
            String result =command1.execute();
            System.out.println(result);
            System.out.println(command1.isCircuitBreakerOpen());
//            System.out.println(command1.getFailedExecutionException());
        }


    }

    @Test
    public void observable(){
        HystrixObservableCommand<String> command1 = new HystrixObservableCommand<String>(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")) {

            @Override
            protected Observable<String> construct() {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> observer) {
                        try {
                            System.out.println("in call of construct! threadPool:" + Thread.currentThread().getName());
                            if (!observer.isUnsubscribed()) {
//                      observer.onError(getExecutionException());  // 直接抛异常退出，不会往下执行
                                observer.onNext("Hello1" + " threadPool:" + Thread.currentThread().getName());
                                observer.onNext("Hello2" + " threadPool:" + Thread.currentThread().getName());
                                observer.onNext(" threadPool:" + Thread.currentThread().getName());
                                System.out.println("complete before------" + " threadPool:" + Thread.currentThread().getName());
                                observer.onCompleted(); // 不会往下执行observer的任何方法
                                System.out.println("complete after------" + " threadPool:" + Thread.currentThread().getName());
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

}
