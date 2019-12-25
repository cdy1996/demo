package com.cdy.demo.framework.reactor;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * todo
 * Created by 陈东一
 * 2019/9/22 0022 14:39
 */
public class ReactorDemo {


    public static void main(String[] args) {
        String key = "message";
//        Mono.from()
        Mono<String> stringMono = Mono.just("Hello")
//                        .flatMap( s -> Mono.subscriberContext()
//                                .map( ctx -> {
//                                    System.out.println(Thread.currentThread().getName() +  ctx.get(key));
//                                    return s + " " + ctx.get(key);
//                                })
//                        )
//                        .publishOn(Schedulers.elastic())
                .flatMap(s -> Mono.subscriberContext()
                        .map(ctx -> {
                            System.out.println(Thread.currentThread().getName() + ctx.get(key));
                            return s + " " + ctx.get(key);
                        })
                        .subscriberContext(ctx -> ctx.put(key, "Reactor"))
                );
        Mono<String> r = stringMono.subscriberContext(ctx -> ctx.put(key, "World"));
        Disposable subscribe = r.subscribe(e -> System.out.println(e));

//        StepVerifier.create(r)
//                .expectNext("Hello World Reactor")
//                .verifyComplete();

    }


    public static void main1(String[] args) {
        Mono.just("Hello")
                .map(s -> {
                    return Mono.just(s + s);
                })
                .subscribe(e -> {
                    e.subscribe(ee -> System.out.println(ee));
                });
    }

}
