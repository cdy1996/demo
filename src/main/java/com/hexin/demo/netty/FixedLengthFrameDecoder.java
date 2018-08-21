package com.hexin.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by viruser on 2018/6/11.
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        while (byteBuf.readableBytes() >= frameLength) {
            System.out.println("读取3个字节");
            ByteBuf buf = byteBuf.readBytes(frameLength);
            list.add(buf);
        }
    }

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException(
                    "frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }
}
