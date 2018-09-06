package com.hexin.demo.netty.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by viruser on 2018/6/7.
 */
public class ByteBufTest {

    public static void main(String[] args) {
        Channel channel = new NioServerSocketChannel();
        ByteBufAllocator alloc = channel.alloc();
        ByteBuf byteBuf = alloc.directBuffer();
        int i = byteBuf.refCnt();



//        ChannelHandlerContext channelHandlerContext = new HeadContext();
//        ByteBufAllocator alloc1 = channelHandlerContext.alloc();



    }
}
