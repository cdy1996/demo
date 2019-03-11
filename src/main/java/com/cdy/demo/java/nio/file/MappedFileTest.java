package com.cdy.demo.java.nio.file;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件映射的操作
 * Created by 陈东一
 * 2019/3/9 0009 20:33
 */
public class MappedFileTest {
    
    Path path = Paths.get("D:\\workspace\\ideaworkspace\\练手项目\\springstudy\\src\\test\\resource\\abc.txt");
    Path outPath = Paths.get("D:\\workspace\\ideaworkspace\\练手项目\\springstudy\\src\\test\\resource\\ccc.txt");
    
    String s = "aaaaaaaaaaaaaaaaaaaaaaaaa";
    @Test
    public void test() throws FileNotFoundException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), "r");
        RandomAccessFile outFile = new RandomAccessFile(outPath.toFile(), "rw");

//        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.READ)) {
        try (FileChannel channel =randomAccessFile.getChannel(); FileChannel outChannel =outFile.getChannel()) {
    
            MappedByteBuffer mapBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 30);
    
            CharBuffer charBuffer = mapBuffer.asCharBuffer();
            System.out.println(charBuffer.toString());
//            mapBuffer.put(s.getBytes());
    
//            byte[] array = mapBuffer.array();
    
//            System.out.println(new String(array));
    
            outChannel.write(mapBuffer);
            mapBuffer.force();
    
            System.out.println("!23");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
