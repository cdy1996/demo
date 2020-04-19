package com.cdy.demo.repeatedWheels.mykvdb;

/**
 * todo
 * Created by 陈东一
 * 2020/4/19 0019 15:38
 */
public class IndexBlock {
    private byte[] lastKV;
    
    // 该DataBlock 在DIskFile中的偏移位置
    private long offset;
    
    // 该DataBlock占用的长度
    private long size;
    
    // 该DataBlock所有KeyValue计算出来的布隆过滤器字节数组
    private byte[] bloomFilter;
    
}
