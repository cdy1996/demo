package com.cdy.demo.framework.rx.rxjava2.token;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;

import java.util.concurrent.Callable;

@Slf4j
public class Demo {

    private Observable<String> getUserObservable(final int index, final String token) {
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                log.info(index + "使用token=" + token + "发起请求");
                //模拟根据Token去请求信息的过程。
                if (!TextUtils.isEmpty(token) && System.currentTimeMillis() - Long.valueOf(token) < 2000) {
                    e.onNext(index + ":" + token + "的用户信息");
                } else {
                    e.onError(new Throwable("ERROR_TOKEN"));
                }
            }
        });
    }

    private void startRequest(final int index) {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                String cacheToken = TokenLoader.getInstance().getCacheToken();
                log.info(index + "获取到缓存Token=" + cacheToken);
                return Observable.just(cacheToken);
            }
        }).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String token) throws Exception {
                return getUserObservable(index, token);
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {

            private int mRetryCount = 0;

            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {

                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        log.info(index + ":" + "发生错误=" + throwable + ",重试次数=" + mRetryCount);
                        if (mRetryCount > 0) {
                            return Observable.error(new Throwable("ERROR_RETRY"));
                        } else if ("ERROR_TOKEN".equals(throwable.getMessage())) {
                            mRetryCount++;
                            return TokenLoader.getInstance().getNetTokenLocked();
                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        });
        DisposableObserver<String> observer = new DisposableObserver<String>() {

            @Override
            public void onNext(String value) {
                log.info(index + ":" + "收到信息=" + value);
            }

            @Override
            public void onError(Throwable e) {
                log.info(index + ":" + "onError=" + e);
            }

            @Override
            public void onComplete() {
                log.info(index + ":" + "onComplete");
            }
        };
        observable.subscribeOn(Schedulers.io()).subscribe(observer);
    }
}
