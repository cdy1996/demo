package com.cdy.demo.java.nio;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * nio
 * Created by viruser on 2018/5/30.
 */
public class NioTest {

    public static void main(String[] args) throws IntrospectionException, InterruptedException, IOException {
        RandomAccessFile file = new RandomAccessFile("C:\\Users\\viruser.v-desktop\\Desktop\\123.txt","r");
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = channel.read(buffer);
        while (read != -1) {
            buffer.flip();// 切换模式，写->读
            byte[] array = buffer.array();
            System.out.println(new String(array,"gbk"));
            System.out.print(Charset.forName("gbk").decode(buffer));  // 这样读取，如果10字节恰好分割了一个字符将出现乱码
            buffer.clear();// 清空,position位置为0，limit=capacity
            //  继续往buffer中写
            read = channel.read(buffer);
        }
        file.close();

    }


}
