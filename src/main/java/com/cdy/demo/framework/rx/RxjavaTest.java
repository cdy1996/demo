package com.cdy.demo.framework.rx;

import io.reactivex.*;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;


public class RxjavaTest {


    @Test
    public void test() {
        Observable<LoginApiBean> observable = Observable.create(new ObservableOnSubscribe<LoginApiBean>() {
            @Override
            public void subscribe(ObservableEmitter<LoginApiBean> e) throws Exception {
                System.out.println(Thread.currentThread().getName() + " 用户登录");
                e.onNext(login());
            }
        });

//       1.x 写法 BlockingObservable.from(observable).toFuture();
        UserInfoBean userInfoBean = observable.blockingFirst().getUserInfoBean();

        System.out.println(userInfoBean);

    }

    @Test
    public void flowable() {


        Flowable.create(new FlowableOnSubscribe<LoginApiBean>() {
            @Override
            public void subscribe(FlowableEmitter<LoginApiBean> e) throws Exception {
                System.out.println(Thread.currentThread().getName() + " 用户登录");
                e.onNext(login());
                e.onNext(login());
                e.onNext(login());
                e.onNext(login());
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER) //调用登录接口
                .observeOn(Schedulers.newThread())  //调度线程
                .map(new Function<LoginApiBean, UserInfoBean>() {

                    @Override
                    public UserInfoBean apply(LoginApiBean loginApiBean) {
                        //处理登录结果，返回UserInfo
                        if (loginApiBean.isSuccess()) {
                            System.out.println(Thread.currentThread().getName() + "处理登录结果，返回UserInfo");
                            return loginApiBean.getUserInfoBean();
                        } else {
                            throw new RequestFailException("获取网络请求失败");
                        }
                    }
                })
                .doOnNext(bean -> {    //保存登录结果UserInfo
                            System.out.println(Thread.currentThread().getName() + "保存登录结果UserInfo");
                            saveUserInfo(bean);
                        }
                )
                .subscribeOn(Schedulers.io())   //调度线程
                .observeOn(Schedulers.newThread())  //调度线程
                .subscribe(new Subscriber<UserInfoBean>() {

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        subscription.request(3);
                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        System.out.println(Thread.currentThread().getName() + " 整个请求成功，根据获取的UserInfo更新对应的Vie");
                        showSuccessView(userInfoBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //请求失败，显示对应的View
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        try {
            System.out.println("主线程结束");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showFailView() {

    }

    public static void showSuccessView(UserInfoBean bean) {

    }

    public static void saveUserInfo(UserInfoBean bean) {

    }

    public static LoginApiBean login() {
        return new LoginApiBean();
    }


    @Test
    public void testObservable() {
        Observable.create(new ObservableOnSubscribe<LoginApiBean>() {
            @Override
            public void subscribe(ObservableEmitter<LoginApiBean> e) throws Exception {
                System.out.println(Thread.currentThread().getName() + " 用户登录");
                e.onNext(login());
            }
        }) //调用登录接口
                .observeOn(Schedulers.newThread())  //调度线程
                .map(new Function<LoginApiBean, UserInfoBean>() {

                    @Override
                    public UserInfoBean apply(LoginApiBean loginApiBean) {
                        //处理登录结果，返回UserInfo
                        if (loginApiBean.isSuccess()) {
                            System.out.println(Thread.currentThread().getName() + "处理登录结果，返回UserInfo");
                            return loginApiBean.getUserInfoBean();
                        } else {
                            throw new RequestFailException("获取网络请求失败");
                        }
                    }
                })
                .doOnNext(bean -> {    //保存登录结果UserInfo
                            System.out.println(Thread.currentThread().getName() + "保存登录结果UserInfo");
                            saveUserInfo(bean);
                        }
                )
                .subscribeOn(Schedulers.io())   //调度线程
                .observeOn(Schedulers.newThread())  //调度线程
                .subscribe(new Consumer<UserInfoBean>() {
                    @Override
                    public void accept(UserInfoBean bean) throws Exception {
                        //整个请求成功，根据获取的UserInfo更新对应的View
                        System.out.println(Thread.currentThread().getName() + " 整个请求成功，根据获取的UserInfo更新对应的Vie");
                        showSuccessView(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //请求失败，显示对应的View
                        throwable.printStackTrace();
                    }
                });


        try {
            System.out.println("主线程结束");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class LoginApiBean {
    public boolean isSuccess() {
        return false;
    }

    public UserInfoBean getUserInfoBean() {
        return null;
    }
}

class UserInfoBean {
}

class RequestFailException extends RuntimeException {
    public RequestFailException(String message) {
        super(message);
    }
}