package com.cdy.demo.framework.netty.nettyInAction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Created by viruser on 2018/6/25.
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> out) throws Exception {
            ByteBuf data = datagramPacket.content();
            int idx = data.indexOf(0, data.readableBytes(),
                    LogEvent.SEPARATOR);
            String filename = data.slice(0, idx)
                    .toString(CharsetUtil.UTF_8);
            String logMsg = data.slice(idx + 1,
                    data.readableBytes()).toString(CharsetUtil.UTF_8);
            LogEvent event = new LogEvent(datagramPacket.sender(),
                    System.currentTimeMillis(), filename, logMsg);
            out.add(event);

    }
}
