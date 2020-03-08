package com.cdy.demo.framework.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * https://www.bilibili.com/video/av35326911?p=62
 * Created by 陈东一
 * 2020/2/29 0029 15:42
 */
public class ReactorTest {
    
    
    public <T> Flux<T> appendCustomError(Flux<T> source){
        return source.concatWith(Mono.error(new IllegalArgumentException("custom")));
    }
    
    @Test
    public void testAppendBoomError(){
        Flux<String> source = Flux.just("foo", "bar");
    
        StepVerifier.create(
                appendCustomError(source))
                .expectNext("foo")
                .expectNext("bar")
                .expectErrorMessage("custom")
                .verify();
        
    }
}
