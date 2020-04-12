package com.cdy.demo.framework.rx.rxjava1;

import rx.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * todo
 * Created by 陈东一
 * 2019/12/28 0028 22:17
 */
public class JoinDemo {
    
    
    public static void main(String[] args) throws IOException {
        Observable<Integer> observable1 = Observable.just(1, 2, 3);
//        Observable<Integer> observable1 = Observable.range(1, 10);
//        Observable<Long> observable1 = Observable.interval(1, TimeUnit.SECONDS);;
        Observable<Integer> observable2 = Observable.just(4, 5, 6);
//        Observable<Integer> observable2 = Observable.range(1, 10);
//        Observable<Long> observable2 = Observable.interval(1, TimeUnit.SECONDS);


//
//        Observable.merge(observable1, observable2)
////                .subscribeOn(Schedulers.immediate())
//                .subscribe(x -> System.out.println("onNext: " + x));
//        System.out.println();
//        observable1.mergeWith(observable2)
//                .subscribe(x -> System.out.println("onNext: " + x));
        
        observable1.join(observable2,
                x -> Observable.just(x).delay(0, TimeUnit.MILLISECONDS), //错误用法
//                x -> Observable.never(),
                y -> Observable.just(y), //错误用法
//                y -> Observable.timer(200, TimeUnit.MILLISECONDS),
                (x, y) -> x.toString() + "-" + y.toString()
        )
                .subscribe(x -> System.out.println("onNext: " + x));
//
        
        
        System.in.read();
        
    }
    
    public static void test() {
        //输出[0,1,2,3]序列
        Observable<Integer> ob = Observable.create(subscriber -> {
            for (int i = 0; i < 4; i++) {
                subscriber.onNext(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Observable.just("hello")
                .join(ob,
                        s -> {
                            System.out.println("1->" + s);
                            //使Observable延迟3000毫秒执行
                            return Observable.timer(3000, TimeUnit.MILLISECONDS);
                            
                        },
                        integer -> {
                            System.out.println("2->" + integer + "");
                            //使Observable延迟2000毫秒执行
                            return Observable.timer(2000, TimeUnit.MILLISECONDS);
                        },
                        
                        (s, integer) -> s + integer ////结合上面发射的数据
                )
                .subscribe(o -> System.out.println("sum: " + o));
    }
}
