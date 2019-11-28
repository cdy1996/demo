package com.cdy.demo.framework.reactor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.TcpServer;

import java.io.IOException;

/**
 * todo
 * Created by 陈东一
 * 2019/7/22 0022 23:12
 */
public class ReactorNetty {
    
    
    public static void main(String[] args) throws IOException {
    
        TcpServer.create()
                .bootstrap(e->e.childHandler(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        super.channelRead(ctx, msg);
                    }
                }))
            
                .host("127.0.0.1")
                .port(1234)
                .bind()
                .block();
        
         HttpServer.create()
                .host("127.0.0.1")
                .port(8080)
                .route(r->
                        r.get("/a", (request, response)->{
                            System.out.println("ddd");
                    return response.sendString(request.receive()
                            .asString()
                            .map(it -> {
                                System.out.println(request.param("param"));
                                return it + ' ' + request.param("param") + '!';
                            })
                            .doOnEach(System.out::println));
                }))
//                .handle((request, response)-> response.sendString(request.receive()
//                        .asString()
//                        .log("all")
//                        .map(it -> it + ' ' + request.param("param") + '!')
//                        .log("all")))
                .bindNow();

         System.in.read();
        
        
    }
}
