package com.cdy.demo.java.juc;

import java.util.concurrent.ConcurrentHashMap;

/**
 * todo
 * Created by 陈东一
 * 2019/4/20 0020 19:46
 */
public class ConcurrentHashMapTest {
    
    
    public static void main(String[] args) {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
    
        String put = map.put("123", "123");
        System.out.println(put);
    
        String put1 = map.put("123", "333");
        System.out.println(put1);
        
        map.clear();
        String put2 = map.put("123", "444");
        System.out.println(put2);
        
        
    
    
    }
}
