package com.cdy.demo.java.nio.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 文件监听  1.7之前实现
 * Created by 陈东一
 * 2018/5/25 20:21
 */
public class FileMonitioring {
    //用户目录和用户工作目录
    
    public static void main(String[] args) throws InterruptedException {
//        File usrDir = new File("src/main");
//        println("用户目录" + System.getProperty("user.home"));
//        println("用户工作目录" + usrDir.getAbsolutePath());
        println("用户工作目录" + System.getProperty("user.dir"));
        
        File userDir = new File(System.getProperty("user.dir"));

        List<String> subFiles = list(userDir.list());
        
        long lastModified = userDir.lastModified();
        
        while (true) {
            if (lastModified == userDir.lastModified()) {
                continue;
            }
            
            lastModified = userDir.lastModified();
            //保存现有文件
            List<String> newSubFiles = list(userDir.list());
            //首先复制一份现有的文件
            List<String> remainSubFile = list(userDir.list());
            //删除上一次的文件  --》 剩下的是新增文件
            newSubFiles.removeAll(subFiles);
            
            System.out.println("新增文件");
            Stream.of(newSubFiles).forEach(FileMonitioring::println);

            System.out.println("删除文件");

            subFiles = newSubFiles;
            
            
            Thread.sleep(500L);
            
        }
        
        
    }
    
    private static <T> List<T> list(T... values) {
        return new ArrayList<T>(Arrays.asList(values));
    }
    
    private static void println(Object object) {
        System.out.println(object);
        
    }
}
