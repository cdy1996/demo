package com.cdy.demo.framework.rx.rsocket;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.rsocket.*;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.WebsocketDuplexConnection;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.util.ByteBufPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.Connection;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

import java.time.Duration;
import java.util.HashMap;

public class WebSocketHeadersSample {
  static final Payload payload1 = ByteBufPayload.create("Hello ");

  public static void main(String[] args) {

    ServerTransport.ConnectionAcceptor acceptor =
        RSocketFactory.receive()
            .frameDecoder(PayloadDecoder.ZERO_COPY)
            .acceptor(new SocketAcceptorImpl())
            .toConnectionAcceptor();

    DisposableServer disposableServer =
        HttpServer.create()
            .host("localhost")
            .port(0)
            .route(
                routes ->
                    routes.ws(
                        "/",
                        (in, out) -> {
                          if (in.headers().containsValue("Authorization", "test", true)) {
                            DuplexConnection connection =
                                new WebsocketDuplexConnection((Connection) in);
                            return acceptor.apply(connection).then(out.neverComplete());
                          }

                          return out.sendClose(
                              HttpResponseStatus.UNAUTHORIZED.code(),
                              HttpResponseStatus.UNAUTHORIZED.reasonPhrase());
                        }))
            .bindNow();

    WebsocketClientTransport clientTransport =
        WebsocketClientTransport.create(disposableServer.host(), disposableServer.port());

    clientTransport.setTransportHeaders(
        () -> {
          HashMap<String, String> map = new HashMap<>();
          map.put("Authorization", "test");
          return map;
        });

    RSocket socket =
        RSocketFactory.connect()
            .keepAliveAckTimeout(Duration.ofMinutes(10))
            .frameDecoder(PayloadDecoder.ZERO_COPY)
            .transport(clientTransport)
            .start()
            .block();

    Flux.range(0, 100)
        .concatMap(i -> socket.fireAndForget(payload1.retain()))
        //        .doOnNext(p -> {
        ////            System.out.println(p.getDataUtf8());
        //            p.release();
        //        })
        .blockLast();
    socket.dispose();

    WebsocketClientTransport clientTransport2 =
        WebsocketClientTransport.create(disposableServer.host(), disposableServer.port());

    RSocket rSocket =
        RSocketFactory.connect()
            .keepAliveAckTimeout(Duration.ofMinutes(10))
            .frameDecoder(PayloadDecoder.ZERO_COPY)
            .transport(clientTransport2)
            .start()
            .block();

    // expect error here because of closed channel
    rSocket.requestResponse(payload1).block();
  }

  private static class SocketAcceptorImpl implements SocketAcceptor {
    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setupPayload, RSocket reactiveSocket) {
      return Mono.just(
          new AbstractRSocket() {

            @Override
            public Mono<Void> fireAndForget(Payload payload) {
              //                  System.out.println(payload.getDataUtf8());
              payload.release();
              return Mono.empty();
            }

            @Override
            public Mono<Payload> requestResponse(Payload payload) {
              return Mono.just(payload);
            }

            @Override
            public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
              return Flux.from(payloads).subscribeOn(Schedulers.single());
            }
          });
    }
  }
}