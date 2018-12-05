package com.hexin.demo.netty;

import com.hexin.demo.netty.study.FixedLengthFrameDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * Created by viruser on 2018/6/11.
 */
public class EmbeddedChannelTest {

    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
            buf.writeCharSequence("netty通道测试", Charset.defaultCharset());
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println("入站  read");
                        System.out.println(msg);
                        ReferenceCountUtil.release(msg);
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("入站  readComplete");
                        ctx.fireChannelRead("123");
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println("入站  exceptionCaught");
                        super.exceptionCaught(ctx, cause);
                    }
                },
                new ChannelOutboundHandlerAdapter(){
//                    @Override
//                    public void read(ChannelHandlerContext ctx) throws Exception {
//                        System.out.println("出站  read");
//                        super.read(ctx);
//                    }

                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        System.out.println("出站  write");
                        ChannelFuture channelFuture = ctx.writeAndFlush("3333");
                        channelFuture.addListener((ChannelFutureListener) future -> {
                            System.out.println("出站"+future.isSuccess());
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                                future.channel().close();
                            }
                        });
                    }


                },
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        System.out.println("入站2  read");
                        System.out.println(msg);
                        ReferenceCountUtil.release(msg);
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("入站2  channelReadComplete");
                        ChannelFuture skr = ctx.writeAndFlush("skr");
                        skr.addListener((ChannelFutureListener) future -> {
                            System.out.println("入站2"+future.isSuccess());
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                                future.channel().close();
                            }
                        });
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println("入站2  exceptionCaught");
                        cause.printStackTrace();
                        ctx.channel().close();
                    }
                },
                new ChannelOutboundHandlerAdapter(){
//                    @Override
//                    public void read(ChannelHandlerContext ctx) throws Exception {
//                        System.out.println("出站2  read");
//                        super.read(ctx);
//                    }

                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        System.out.println("出站2  write");
                        super.write(ctx, msg, promise);
                    }
                }
                );
        // write bytes
        channel.writeInbound(input.retain());
        channel.writeOutbound("!23");
        channel.finish();
        // read messages
        String read = channel.readOutbound();
//        ByteBuf x = buf.readSlice(3);
//        System.out.println(x.toString(CharsetUtil.UTF_8));
        System.out.println(read);
//        assertEquals(x, read);
//
//        read = (ByteBuf) channel.readInbound();
//        assertEquals(buf.readSlice(3), read);
//        read.release();
//
//        read = (ByteBuf) channel.readInbound();
//        assertEquals(buf.readSlice(3), read);
//        read.release();
//
//        assertNull(channel.readInbound());
//        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(
                new FixedLengthFrameDecoder(3));
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));
        assertTrue(channel.finish());
        ByteBuf read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        read = (ByteBuf) channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();
        assertNull(channel.readInbound());
        buf.release();
    }
}
