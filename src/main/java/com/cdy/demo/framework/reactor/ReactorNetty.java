package com.cdy.demo.framework.reactor;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

/**
 * todo
 * Created by 陈东一
 * 2019/7/22 0022 23:12
 */
public class ReactorNetty {
    
    
    public static void main(String[] args) {
        Mono<? extends DisposableServer> bind = HttpServer.create()
                .host("127.0.0.1")
                .port(8080)
                .bind();
        
        
    }
}
