package com.hexin.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by viruser on 2018/6/7.
 */
@ChannelHandler.Sharable
public class EchoClientHandler2 extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当被通知channel是活跃时，发送一条消息
//        ctx.writeAndFlush(Unpooled.copiedBuffer("netty roks!", CharsetUtil.UTF_8));
        System.out.println("EchoClientHandler2 channelActive in");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("EchoClientHandler2 messageReceived in");
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
