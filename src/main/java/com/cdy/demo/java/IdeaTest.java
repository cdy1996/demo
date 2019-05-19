package com.cdy.demo.java;

import java.util.Arrays;

/**
 * todo
 * Created by 陈东一
 * 2019/5/11 0011 10:25
 */
public class IdeaTest {
    
    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        System.out.println(System.getenv("test"));
        System.out.println(System.getProperty("test"));
    }
}
