package com.cdy.demo.framework.reactor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * reactor学习
 * https://htmlpreview.github.io/?https://github.com/get-set/reactor-core/blob/master-zh/src/docs/
 * Created by 陈东一
 * 2019/9/22 0022 14:39
 */
public class ReactorDemo {

    @Before
    public void before(){
        Hooks.onOperatorDebug();
    }

    @After
    public void sleep() throws InterruptedException {
        Thread.sleep(10000L);
    }



    @Test
    public void testJoin() throws IOException {
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
    @Test
    public void testUsing() {
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


        // wheninner 吃掉了所有的onNext , 所有的publish完成
//        Mono.when(Flux.just(1).doOnNext(e-> System.out.println(e)),
//                Flux.just(1, 2).doOnNext(e-> System.out.println(e)))
//                .subscribe(e -> System.out.println(e));
//        Mono.whenDelayError(Flux.just(1).doOnNext(e-> System.out.println(e)),
//                Flux.just(1, 2).doOnNext(e-> System.out.println(e)))
//                .subscribe(e -> System.out.println(e));
        // 当 n 个 Mono 都终止时返回
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


    @Test
    public void testPublish() {
        Flux<Integer> just = Flux.just(1, 2, 3).doOnNext(e -> System.out.println("just"));
        Flux<Integer> just1 = just.flatMap(e -> Flux.just(e + 10, e + 100, e + 1000).doOnNext(ee -> System.out.println("just1" + ee)));
        // 这样 just会被订阅两次
        Flux.merge(just, just1)
                .subscribe(e -> System.out.println(e));
        // 因为just1来源于just, 所以通过publish 可以进行共享上流
        just.publish(t ->
                t.mergeWith(
                        t.flatMap(e -> Flux.just(e + 10, e + 100, e + 1000)
                                .doOnNext(ee -> System.out.println("just1" + ee))))
        )
                .subscribe(e -> System.out.println(e));

        // 特殊的优化
        just.flatMap(e -> Flux.just(e, e + 10, e + 100, e + 1000))
                .subscribe(e -> System.out.println(e));
    }

    // 通过connect后开始释放流, 订阅者订阅后获取热流
    @Test
    public void testPublish2() {
        Flux<Integer> publish = Flux.just(1, 2, 3, 4, 5, 6, 7, 8)
                .publish(2)
                .autoConnect()
                .subscribeOn(Schedulers.elastic());
        publish
                .subscribe(e -> System.out.println(e));
        publish
                .subscribe(e -> System.out.println(e));
    }


    @Test
    // 通过伴身流 去判断是否需要重复 执行. 当伴身流完成时就结束
    public void testRepeatWhen() {
        Flux.just(1, 2)
//                .repeatWhen(e -> e.take(3)) // 多重复3次
//                .repeatWhen(e -> e.delayElements(Duration.ofMillis(500L))) // 每次延迟500毫秒在重复
                .repeatWhen(e -> e.takeUntil(ee -> ee == 2L)) //
                .subscribe(e -> System.out.println(e));


    }

    @Test
    public void testRepeat() {
        Mono.just(1)
                .repeat(3)
                .subscribe(e -> System.out.println(e));


    }

    @Test
    public void testRetry() {
        AtomicInteger integer = new AtomicInteger(1);
        Mono.fromSupplier(() -> {
            int andIncrement = integer.getAndIncrement();
            if (andIncrement == 2) {
                return 1;
            } else {
                throw new RuntimeException("123");
            }
        })
                .retry()
                .subscribe(e -> System.out.println(e), e -> e.printStackTrace());


//        Mono<Object> retry = TestPublisher.create()
//                .error(new RuntimeException())
//                .mono()
//                .retry();
//        StepVerifier.create(retry)
//                .expectError();

    }

    @Test //https://blog.csdn.net/weweeeeeeee/article/details/82885449
    public void testRetryWhen() {
        Flux<String> flux = Flux
                .<String>error(new IllegalArgumentException())
//                .doOnError(System.out::println)
                .retryWhen(companion -> companion.take(3)); // 这个最后是空结果
//                .retry(3);  // 这个最后返回失败

        flux.subscribe(e -> System.out.println(e)/*, e -> System.out.println(e)*/  );


        // 类似retrywhen, 但是可以获取到原始的异常
        Flux<String> flux2 =
                Flux.<String>error(new IllegalArgumentException())
                        .retryWhen(companion -> companion
                                .zipWith(Flux.range(1, 4),
                                        (error, index) -> {
                                            if (index < 4) return index;
                                            else throw Exceptions.propagate(error);
                                        })
                        );

    }

    @Test
    public void testError() {
        AtomicInteger integer = new AtomicInteger(1);
        Mono.fromSupplier(() -> {
            int andIncrement = integer.getAndIncrement();
            if (andIncrement == 2) {
                return andIncrement + "";
            } else {
                throw new RuntimeException("123");
            }
        })
                .onErrorContinue((e,o)->e.getMessage())
//                .onErrorResume(e -> Mono.just(e.getMessage()))
//                .onErrorReturn("error")
                .subscribe(e -> System.out.println(e), e -> e.printStackTrace());


    }

    // 失去的时间, 代表每个元素之间的间隔
    @Test
    public void testElapsed() throws InterruptedException {
        Mono.delay(Duration.ofSeconds(1L)).subscribe(e -> System.out.println(e));

        System.out.println();
        // 通过 Mono.delay来实现延迟
        Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1L))
                .elapsed()
                .subscribe(e -> System.out.println(e));

        Thread.sleep(5000L);
        System.out.println();
        Flux.interval(Duration.ofSeconds(1L))
                .elapsed()
                .subscribe(e -> System.out.println(e));
    }

    // 缓冲
    @Test
    public void testBufferWhen() {
        // 原始数据每秒发送一个  这里设定时间窗口 500毫秒 ,
        Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1L))
                .bufferWhen(Flux.interval(Duration.ofMillis(500L)), e -> {
                    return Mono.delay(Duration.ofMillis(500L));
                })
                .subscribe(e -> System.out.println(e));

        // 类似window
        Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofSeconds(1L))
                .window(Duration.ofMillis(500L))
                .subscribe(e -> e.buffer().subscribe(ee -> System.out.println(ee)));

    }

    // 和buffer的区别在 buffer在于聚集, 而cache并不聚集
    @Test
    public void testCache() throws InterruptedException {
        Flux<Integer> cache = Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofMillis(500L))
                .cache(Duration.ofSeconds(1L));

        cache.subscribe(e -> System.out.println(e));
        Thread.sleep(1000L);
        cache.subscribe(e -> System.out.println(e));


    }


    @Test
    public void testmMaterialize() throws InterruptedException {
        Mono.just(1)
                .materialize()
                .subscribe(e -> System.out.println(e.getType()));


    }


    @Test
    public void testGenerate(){
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(e -> System.out.println(e));

    }

    MyEventProcessor myEventProcessor = new MyEventProcessor();
    @Test
    public void testCreate(){
        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<String>() {
                        // 推
                        public void onDataChunk(List<String> chunk) {
                            for(String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });

            sink.onRequest(n -> {  // 拉
                List<String> messages = myEventProcessor.request(n);
                for(String s : messages) {
                    sink.next(s);
                }
            });
        });

    }

    @Test
    public void testPush(){
        Flux<String> bridge = Flux.push(sink -> {
            myEventProcessor.register(
                    new SingleThreadEventListener<String>() {

                        public void onDataChunk(List<String> chunk) {
                            for(String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }

                        public void processError(Throwable e) {
                            sink.error(e);
                        }
                    });
        });

    }

    @Test
    public void testHandler(){
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null)
                        sink.next(letter);
                });

        alphabet.subscribe(System.out::println);
    }
    public String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }

    @Test
    public void testContext() {
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

    /**
     * userService.getFavorites(userId)
     *            .timeout(Duration.ofMillis(800))  //超时限定
     *            .onErrorResume(cacheService.cachedFavoritesFor(userId)) //错误的话就走缓存
     *            .flatMap(favoriteService::getDetails)  //获取对应的详情
     *            .switchIfEmpty(suggestionService.getSuggestions()) //如果详情为空就获取显示别的
     *            .take(5) //限制5条
     *            .publishOn(UiUtils.uiThreadScheduler())
     *            .subscribe(uiList::show, UiUtils::errorPopup);
     */
    @Test
    public void testContext1(){
        String key = "message";

        /*Mono<String> r =*/ Mono.subscriberContext()
                .map(ctx -> ctx.put(key, "Hello"))
                .doOnNext(e -> System.out.println("1"+e.get(key).toString()))
                .flatMap(ctx -> Mono.subscriberContext()) //flatmap 会重新订阅一次, 所以原来额的context没法传递下来
                .map(ctx -> ctx.getOrDefault(key, "Default"))
                .subscribe(e -> System.out.println(e));

//        StepVerifier.create(r)
//                .expectNext("Default")
//                .verifyComplete();

    }

    @Test
    public void testContext2(){
        Mono<String> put = doPut("www.example.com", Mono.just("Walter"))
                .subscriberContext(Context.of(HTTP_CORRELATION_ID, "2-j3r9afaf92j-afkaf"))
                .filter(t -> t.getT1() < 300)
                .map(Tuple2::getT2);

        StepVerifier.create(put)
                .expectNext("PUT <Walter> sent to www.example.com with header X-Correlation-ID = 2-j3r9afaf92j-afkaf")
                .verifyComplete();

    }
    static final String HTTP_CORRELATION_ID = "reactive.http.library.correlationId";

    Mono<Tuple2<Integer, String>> doPut(String url, Mono<String> data) {
        // ZipCoordinator 包含最后的订阅者, 它被订阅后等待 ZipInner推数据过来
        // ZipInner 订阅原始流和MonoContext流, 然后request在把数据都给ZipCoordinator再到最后的订阅者
        Mono<Tuple2<String, Optional<Object>>> dataAndContext =
                data.zipWith(Mono.subscriberContext()
                        .map(c -> c.getOrEmpty(HTTP_CORRELATION_ID)));

        return dataAndContext
                .<String>handle((dac, sink) -> {
                    if (dac.getT2().isPresent()) {
                        sink.next("PUT <" + dac.getT1() + "> sent to " + url + " with header X-Correlation-ID = " + dac.getT2().get());
                    }
                    else {
                        sink.next("PUT <" + dac.getT1() + "> sent to " + url);
                    }
                    sink.complete();
                })
                .map(msg -> Tuples.of(200, msg));
    }

    public Flux<String> processOrFallback(Mono<String> source, Publisher<String> fallback) {
        return source
                .flatMapMany(phrase -> Flux.fromArray(phrase.split("\\s+")))
                .switchIfEmpty(fallback); //当complete时没有处理过数据那么就订阅新的
    }

    @Test
    public void testSwitchIfEmpty(){
        StepVerifier.create(processOrFallback(Mono.empty(), Mono.just("EMPTY_PHRASE")))
                .expectNext("EMPTY_PHRASE")
                .verifyComplete();
    }
}

interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
}
class SingleThreadEventListener<T> implements MyEventListener<T>{

    @Override
    public void onDataChunk(List<T> chunk) {
    }

    @Override
    public void processComplete() {

    }
}


class MyEventProcessor{
    List<MyEventListener<?>> list = new ArrayList<>();

    public void register(MyEventListener<?> listener){
        list.add(listener);
    }

    public List<String> request(long n) {
        return null;
    }
}