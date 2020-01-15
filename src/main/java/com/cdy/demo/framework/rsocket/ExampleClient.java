package com.cdy.demo.framework.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

public class ExampleClient {

    public static void main(String[] args) {
        WebsocketClientTransport ws = WebsocketClientTransport.create(URI.create("ws://rsocket-demo.herokuapp.com/ws"));
        RSocket client = RSocketFactory.connect().keepAlive().transport(ws).start().block();

        try {
            Flux<Payload> s = client.requestStream(DefaultPayload.create("peace"));

            s.take(10).doOnNext(p -> System.out.println(p.getDataUtf8())).blockLast();
        } finally {
            client.dispose();
        }
    }


    public static void testZerocopy(){
        RSocketFactory.receive()
                // Enable Zero Copy
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .acceptor(new PingHandler())
                .transport(TcpServerTransport.create(7878))
                .start()
                .block()
                .onClose()
                .block();


        Mono<RSocket> client =
                RSocketFactory.connect()
                        // Enable Zero Copy
                        .frameDecoder(PayloadDecoder.ZERO_COPY)
                        .transport(TcpClientTransport.create(7878))
                        .start();
    }
}
