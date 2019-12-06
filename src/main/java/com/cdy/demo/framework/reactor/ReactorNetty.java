package com.cdy.demo.framework.reactor;

import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpServer;

import java.io.IOException;

/**
 * todo
 * Created by 陈东一
 * 2019/7/22 0022 23:12
 */
public class ReactorNetty {
    
    
    public static void main(String[] args) throws IOException {

//        http();
        LoopResources loop = LoopResources.create("event-loop", 1, 4, true);
        DisposableServer server =
                TcpServer.create()
//                        .doOnConnection(conn->conn.addHandler())
                        .handle((inbound, outbound) -> inbound.receive().then())
                        .bindNow();


        System.in.read();
        server.onDispose()
                .block();


    }

    private static void http() throws IOException {
        DisposableServer server =
                HttpServer.create()
                        .port(8080)
                        .route(routes ->
                                routes.get("/hello",
                                        (request, response) -> response.sendString(Mono.just("Hello World!")))
                                        .post("/echo",
                                                (request, response) -> response.send(request.receive().retain()))
                                        .get("/path/{param}",
                                                (request, response) -> response.sendString(Mono.just(request.param("param"))))
                                        .ws("/ws",
                                                (wsInbound, wsOutbound) -> wsOutbound.send(wsInbound.receive().retain())))
                        .bindNow();
        System.in.read();

        server.onDispose()
                .block();
    }
}
