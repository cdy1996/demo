package com.hexin.demo.netty.study;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Created by viruser on 2018/6/13.
 */
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        list.add(String.valueOf(integer));
    }
}
