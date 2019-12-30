package com.cdy.demo.framework.reactor;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

/**
 * todo
 * Created by 陈东一
 * 2019/9/22 0022 14:39
 */
public class ReactorDemo {
    
    
    private static void testContext() {
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
    
    
    public static void main(String[] args) throws IOException {
//        testContext();
        
        testJoin();
    }
    
    private static void testJoin() throws IOException {
        Flux<Integer> just = Flux.just(1, 2, 3);
        Flux<Integer> just1 = Flux.just(4, 5, 6);
        just.join(just1,
                x -> Mono.just(x).delay(Duration.ofMillis(0)), //错误用法
                y -> Mono.just(y),
                (x, y) -> x.toString() + "-" + y.toString()
        ).subscribe(x -> System.out.println("onNext: " + x));
        System.in.read();
    }
    
}
