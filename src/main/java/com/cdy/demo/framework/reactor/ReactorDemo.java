package com.cdy.demo.framework.reactor;

import reactor.core.publisher.Flux;

/**
 * todo
 * Created by 陈东一
 * 2019/9/22 0022 14:39
 */
public class ReactorDemo {
    
    
    public static void main(String[] args) {
    
        Flux.just("0", "1", "3")
                .map(e->e+",")
                .map(e->e+"()")
                .subscribe(e->{
                    System.out.println(e);
                });
        
    }
    
}
