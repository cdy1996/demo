package com.hexin.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by viruser on 2018/6/11.
 */
public class AbsIntegerEncoderTest {


    @Test
    public void encode() throws Exception {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buf.writeInt(i * -1);
        }
        EmbeddedChannel channel = new EmbeddedChannel(
                new AbsIntegerEncoder());
        assertTrue(channel.writeOutbound(buf));
        assertTrue(channel.finish());
        // read bytes
        for (int i = 1; i < 10; i++) {
            Integer integer = channel.<Integer>readOutbound();
            assertEquals(i, integer.intValue());
        }
        assertNull(channel.readOutbound());

    }

}