package com.cdy.demo.structure.consistencyhash;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 节点
 * Created by 陈东一
 * 2018/11/4 0004 10:00
 */
@Data
public class Node {
    
    private String domain;
    
    private String ip;
    
    private Map<String, Object> data;
    
    public Node(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
        data = new HashMap<>();
    }
    
    public <T> void put(String key, T value) {
        data.put(key, value);
    }
    
    public void remove(String key) {
        data.remove(key);
    }
    
    public <T> T get(String key) {
        return (T) data.get(key);
    }
    
    //FNV1_32_HASH
    public static long hash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        
        // 如果算出来的值为负数则取其绝对值
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }
    
}
