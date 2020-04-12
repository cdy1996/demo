package com.cdy.demo.framework.rx.rxjava1;

import org.apache.http.util.TextUtils;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * todo
 * Created by 陈东一
 * 2020/4/12 0012 16:02
 */
public class CacheDemo {
    
    public static void main(String[] args) {
        final Observable<String> memoryObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onCompleted();
                subscriber.onNext(null);//模拟内存中没有数据
            }
        });
        final Observable<String> diskObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onCompleted();
                subscriber.onNext(null);//模拟本地文件没有数据
            }
        });
        final Observable<String> netWorkObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onCompleted();
                subscriber.onNext("netWork");//模拟网络上有数据
            }
        });
        
        
        Observable.concat(memoryObservable, diskObservable, netWorkObservable)
                .first(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return !TextUtils.isEmpty(s);//返回第一个非空的数据，就近原则取数据
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("Example_Uesed" + "get data from：" + s);
                    }
                })
        ;
    }
}
