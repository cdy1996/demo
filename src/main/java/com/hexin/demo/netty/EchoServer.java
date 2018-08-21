package com.hexin.demo.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * Created by viruser on 2018/6/7.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {

        int port = 8081;
        new EchoServer(port).start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //创建EventLoopGroup

        EventLoopGroup work = new NioEventLoopGroup();
        EventLoopGroup boss = new NioEventLoopGroup();

        try {
            //创建serverBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //添加handler到channelpipline中
                                    .addLast(serverHandler);
                        }
                    });
            //异步绑定到服务器，阻塞等待绑定完成
            ChannelFuture f = b.bind().sync();
            //获取channel的closefuture，并阻塞当前线程知道它完成
            f.channel().closeFuture().sync();

        }finally {
            Future<?> sync = boss.shutdownGracefully().sync();
            Future<?> sync1 = work.shutdownGracefully().sync();
        }
    }
}
