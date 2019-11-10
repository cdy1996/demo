package com.cdy.demo.java.g1;

import java.util.LinkedList;

/**
 * -Xmx128M -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
 * -XX:+PrintTLAB -XX:+UnlockExperimentalVMOptions -XX:G1LogLevel=finest
 * Created by 陈东一
 * 2019/11/10 0010 15:54
 */
public class TLABTest {
    
    private static final LinkedList<String> strings = new LinkedList<>();
    
    public static void main(String[] args) throws Exception {
        int iteration = 0;
        while (true) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 10; j++) {
                    strings.add(new String("String " + j));
                }
            }
            Thread.sleep(100);
        }
    }
}

/*
    
    TLAB: gc thread: 0x00000000184c5800 [id: 5960] desired_size(期望分配的TLAB大小): 491KB
    slow allocs(慢分配次数): 0
    refill waste(阈值): 7864B
    alloc(堆分配的比例): 0.99999    24576KB
    refills(上一次GC到这一次GC期间retired的TLAB块): 1
    waste(由gc,slow,fast组成) 41.4%
        gc(未使用的TLAB空间): 208608B
        slow(产生新的TLAB时旧TLAB浪费的空间): 0B
        fast(在C1中,发送TLAB retired(产生新的TLAB)时,旧TLAB浪费的空间): 0B
    TLAB: gc thread: 0x00000000178ce800 [id: 18156] desired_size: 491KB slow allocs: 2  refill waste: 7864B alloc: 0.99999    24576KB refills: 4 waste 20.1% gc: 399352B slow: 5680B fast: 0B
    TLAB: gc thread: 0x0000000002aea800 [id: 15220] desired_size: 491KB slow allocs: 3  refill waste: 7864B alloc: 0.99999    24576KB refills: 83 waste  0.0% gc: 0B slow: 13008B fast: 0B
    

 */