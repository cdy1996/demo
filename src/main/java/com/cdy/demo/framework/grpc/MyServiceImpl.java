//package com.cdy.demo.framework.grpc;
//
//import io.grpc.ServerBuilder;
//import io.grpc.stub.StreamObserver;
//
//import java.io.IOException;
//
//public class MyServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
//    @Override
//    public StreamObserver<HelloWorldProtos.HelloStreamRequest> biStream(StreamObserver<HelloWorldProtos.HelloStreamResponse> responseObserver) {
//        return new StreamObserver<HelloWorldProtos.HelloStreamRequest>() {
//
//            //接收请求
//            @Override
//            public void onNext(HelloWorldProtos.HelloStreamRequest streamReq) {
//                System.out.println(streamReq.getReqInfo());
//                //接收请求后就返回一个响应
//                responseObserver.onNext(HelloWorldProtos.HelloStreamResponse.newBuilder().setRespInfo("from server").build());
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            //客户端发送数据完毕
//            @Override
//            public void onCompleted() {
//                //服务端也完毕
//                responseObserver.onCompleted();
//            }
//        };
//    }
//
//    public static void main(String[] args) throws IOException {
//        ServerBuilder.forPort(8080)
//                .addService(new MyServiceImpl())
//                .build()
//                .start();
//
//        System.in.read();
//    }
//}