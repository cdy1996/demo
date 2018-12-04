package com.hexin.demo.netty.study;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by viruser on 2018/6/7.
 */
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {


        System.out.println("channel是活跃时，发送一条消息");
        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copiedBuffer("netty roks!", CharsetUtil.UTF_8));

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("记录已接收消息的转储");
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
