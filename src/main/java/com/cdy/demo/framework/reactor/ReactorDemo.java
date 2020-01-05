package com.cdy.demo.framework.reactor;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.Callable;

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

//        testJoin();

//        testUsing();

//        testPublish();
        testPublish2();
        
        System.in.read();
    }
    
    private static void testJoin() throws IOException {
        Flux<Integer> just = Flux.just(1, 2, 3);
//        Flux<Integer> just1 = Flux.just(4, 5, 6);
//        just.join(just1,
//                x -> Mono.just(x).delay(Duration.ofMillis(0)), //错误用法
//                y -> Mono.just(y),
//                (x, y) -> x.toString() + "-" + y.toString()
//        ).subscribe(x -> System.out.println("onNext: " + x));
        just.subscribe(x -> System.out.println("onNext: " + x));
        just.subscribe(x -> System.out.println("onNext: " + x));
        System.in.read();
    }
    
    
    // using 提供可关闭的资源, usingwhen只取第一个, mono和flux的差异在于第二个参数
    public static void testUsing() {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = () -> {
            System.out.println("callable");
            return "1";
        };
        Mono<String> using = Mono.using(callable, c -> Mono.just(c), c -> {
        });

//        using.subscribe(System.out::println);
//        using.subscribe(System.out::println);
//        Mono.usingWhen(Flux.just(1, 2, 3),
//                e -> Mono.just(e),
//                e -> Mono.just(e),
//                e -> Mono.just(e))
//                .subscribe(e -> System.out.println(e));
//        Flux.usingWhen(Flux.just(1, 2, 3),
//                e -> Flux.just(e, e),
//                e -> Mono.just(e),
//                e -> Mono.just(e))
//                .subscribe(e -> System.out.println(e));
        
        
        // 有点像信号量
        // wheninner 吃掉了所有的onNext , 这是个没有结果的初始操作
//        Mono.when(Flux.just(1).doOnNext(e-> System.out.println(e)),
//                Flux.just(1, 2).doOnNext(e-> System.out.println(e)))
//                .subscribe(e -> System.out.println(e));
//        Mono.whenDelayError(Flux.just(1).doOnNext(e-> System.out.println(e)),
//                Flux.just(1, 2).doOnNext(e-> System.out.println(e)))
//                .subscribe(e -> System.out.println(e));
        
        Mono.when(Flux.just(1).doOnNext(e -> {
                    throw new RuntimeException(e + "");
                }),
                Flux.just(1, 2).doOnNext(e -> System.out.println(e)))
                .subscribe(e -> System.out.println(e), e -> System.out.println(e.getMessage()));
        System.out.println();
        Mono.whenDelayError(Flux.just(1).doOnNext(e -> {
                    throw new RuntimeException(e + "");
                }),
                Flux.just(1, 2).doOnNext(e -> System.out.println(e)))
                .subscribe(e -> System.out.println(e), e -> System.out.println(e.getMessage()));
        
    }
    
    
    public static void testPublish() {
        Flux<Integer> just = Flux.just(1, 2, 3).doOnNext(e -> System.out.println("just"));
        Flux<Integer> just1 = just.flatMap(e -> Flux.just(e + 10, e + 100, e + 1000).doOnNext(ee -> System.out.println("just1" + ee)));
        
        Flux.merge(just, just1)
                .subscribe(e -> System.out.println(e));
        
        just.publish(t ->
                t.mergeWith(
                        t.flatMap(e -> Flux.just(e + 10, e + 100, e + 1000)
                                .doOnNext(ee -> System.out.println("just1" + ee))))
        )
                .subscribe(e -> System.out.println(e));
        
    }
    
    public static void testPublish2() {
        Flux<Integer> publish = Flux.just(1, 2, 3, 4, 5, 6, 7, 8)
                .publish(2)
                .autoConnect()
//                .subscribeOn(Schedulers.elastic())
                ;
        publish
                .subscribe(e -> System.out.println(e));
        publish
                .subscribe(e -> System.out.println(e));
    }
    
}
