package com.cdy.demo.framework.rx.rxjava2.token;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

// https://www.jianshu.com/p/e88e61e1a721
@Slf4j
public class TokenLoader {

    private AtomicBoolean mRefreshing = new AtomicBoolean(false);
    private PublishSubject<String> mPublishSubject;
    private Observable<String> mTokenObservable;

    private TokenLoader() {
        mPublishSubject = PublishSubject.create();
        mTokenObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Thread.sleep(1000);
                log.info("发送Token");
                e.onNext(String.valueOf(System.currentTimeMillis()));
            }
        }).doOnNext(new Consumer<String>() {
            @Override
            public void accept(String token) {
                log.info("存储Token=" + token);
                Store.getInstance().setToken(token);
                mRefreshing.set(false);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                mRefreshing.set(false);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static TokenLoader getInstance() {
        return Holder.INSTANCE;
    }

    public String getCacheToken() {
        return Store.getInstance().getToken();
    }

    public Observable<String> getNetTokenLocked() {
        if (mRefreshing.compareAndSet(false, true)) {
            log.info("没有请求，发起一次新的Token请求");
            startTokenRequest();
        } else {
            log.info("已经有请求，直接返回等待");
        }
        return mPublishSubject;
    }

    private void startTokenRequest() {
        mTokenObservable.subscribe(mPublishSubject);
    }

    private static class Holder {
        private static final TokenLoader INSTANCE = new TokenLoader();
    }

}