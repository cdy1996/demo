package com.cdy.demo.framework.reactor;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * todo
 * Created by 陈东一
 * 2019/9/22 0022 14:39
 */
public class ReactorDemo {
    
    
    public static void main(String[] args) {

        String key = "message";
        Mono<String> r =
                Mono.just("Hello")
                        .flatMap( s -> Mono.subscriberContext()
                                .map( ctx -> s + " " + ctx.get(key))
                        )
                        .flatMap( s -> Mono.subscriberContext()
                                .map( ctx -> s + " " + ctx.get(key))
                                .subscriberContext(ctx -> ctx.put(key, "Reactor"))
                        )
                        .subscriberContext(ctx -> ctx.put(key, "World"));

        StepVerifier.create(r)
                .expectNext("Hello World Reactor")
                .verifyComplete();
        
    }
    
}
