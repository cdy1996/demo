//package com.cdy.demo.framework.grpc;
//
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//
//import java.util.concurrent.TimeUnit;
//
//public class RpcClient {
//    private final ManagedChannel channel;
//    private final GreeterGrpc.GreeterBlockingStub blockingStub;
//
//
//    public RpcClient(String host, int port) {
//        channel = ManagedChannelBuilder.forAddress(host, port)
//                .usePlaintext(true)
//                .build();
//
//        blockingStub = GreeterGrpc.newBlockingStub(channel);
//    }
//
//
//    public void shutdown() throws InterruptedException {
//        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
//    }
//
//    public void greet(String name) {
//        HelloWorldProto.HelloRequest request = HelloWorldProto.HelloRequest.newBuilder().setName(name).build();
//        HelloWorldProto.HelloReply response = blockingStub.sayHello(request);
//        System.out.println(response.getMessage());
//
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        RpcClient client = new RpcClient("127.0.0.1", 50051);
//        for (int i = 0; i < 5; i++) {
//            client.greet("world:" + i);
//        }
//
//    }
//
//}
