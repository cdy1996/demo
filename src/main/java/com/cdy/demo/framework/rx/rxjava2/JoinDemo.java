package com.cdy.demo.framework.rx.rxjava2;


import io.reactivex.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * todo
 * Created by 陈东一
 * 2019/12/30 0030 23:25
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
}

