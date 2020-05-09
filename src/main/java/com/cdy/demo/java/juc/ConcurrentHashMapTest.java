package com.cdy.demo.java.juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前未初始化:
 * = 0  //未指定初始容量
 * > 0  //由指定的初始容量计算而来，再找最近的2的幂次方。
 * //比如传入6，计算公式为6+6/2+1=10，最近的2的幂次方为16，所以sizeCtl就为16。
 * 初始化中：
 * = -1 //table正在初始化
 * = -N //N是int类型，分为两部分，高15位，低16位(M)表示
 * //并行扩容线程数+1，具体在resizeStamp函数介绍。
 * 初始化完成：
 * =table.length * 0.75  //扩容阈值调为table容量大小的0.75倍
 * 原文链接：https://blog.csdn.net/tp7309/article/details/76532366
 * Created by 陈东一
 * 2019/4/20 0020 19:46
 */
public class ConcurrentHashMapTest {
    
    
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        // 单线程的情况下, 一个线程处理完需要分配的转移任务后会重新领取 一份转移任务
        for (int i = 0; i < 256; i++) {
            String put = map.put("123" + i, "123");
        }
        String put1 = map.put("222", "333");
        put1 = map.put("333", "333");
        System.out.println(put1);

        map.clear();
        String put2 = map.put("123", "444");
        System.out.println(put2);


    }
}
