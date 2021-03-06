package com.cdy.demo.framework.netty.nettyInAction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * Created by viruser on 2018/6/12.
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {

    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            int readableBytes = byteBuf.readableBytes();
            if (readableBytes > maxFrameSize) {
            // discard the bytes
                byteBuf.clear();
                throw new TooLongFrameException();
            }
            ByteBuf buf = byteBuf.readBytes(readableBytes);
        list.add(buf);
    }
}
