package com.cdy.demo.structure.myhashmap;

/**
 * Created by 陈东一
 * 2018/2/27 15:20
 */
public class Test {
    
    public static void main(String[] args) {
        MyMap<String, String> myMap = new MyHashMap<String, String>();

        for (int i = 0; i < 21; i++) {
            String put = myMap.put("key" + i, "value" + i);
        }

        System.out.println("=============  get  ==================");

        for (int i = 0; i < 21; i++) {
            System.out.println("key: key" + i + ", value:" + myMap.get("key" + i));
        }
    }
}
