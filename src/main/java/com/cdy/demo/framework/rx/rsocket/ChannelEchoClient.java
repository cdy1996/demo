package com.cdy.demo.framework.rx.rsocket;

import io.rsocket.*;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.local.LocalClientTransport;
import io.rsocket.transport.local.LocalServerTransport;
import io.rsocket.util.ByteBufPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * https://github.com/rsocket/rsocket-java/tree/develop/rsocket-examples/src/main/java/io/rsocket/examples/transport
 */
public final class ChannelEchoClient {
  static final Payload payload1 = ByteBufPayload.create("Hello ");

  public static void main(String[] args) {
    RSocketFactory.receive()
        .frameDecoder(PayloadDecoder.ZERO_COPY)
        .acceptor(new SocketAcceptorImpl())
        .transport(LocalServerTransport.create("localhost"))
        .start()
        .subscribe();

    RSocket socket =
        RSocketFactory.connect()
            .keepAliveAckTimeout(Duration.ofMinutes(10))
            .frameDecoder(PayloadDecoder.ZERO_COPY)
            .transport(LocalClientTransport.create("localhost"))
            .start()
            .block();

    Flux.range(0, 100000000)
        .concatMap(i -> socket.fireAndForget(payload1.retain()))
        //        .doOnNext(p -> {
        ////            System.out.println(p.getDataUtf8());
        //            p.release();
        //        })
        .blockLast();
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