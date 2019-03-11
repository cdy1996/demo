package com.cdy.demo.java;

import java.lang.reflect.Field;

/**
 * integer 交换的陷阱
 * Created by 陈东一
 * 2018/3/15 15:38
 */
public class IntegerTest {
    
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Integer a = 1, b = 2;
        swap2(a, b);
        System.out.println(a + " " + b);
        Integer c = 1;
        System.out.println(a == c);

    }
    
    private static void swap(Integer a, Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field value = Integer.class.getDeclaredField("value");
        value.setAccessible(true);
        int temp = a;  // 1
        value.set(a, b);
        System.out.println(Integer.valueOf(temp) == a);
        System.out.println(a == Integer.valueOf(1));
        System.out.println(a.equals(1));
        value.set(b, temp);
        
        
    }

    private static void swap2(Integer a, Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field value = Integer.class.getDeclaredField("value");
        value.setAccessible(true);
        int temp = a;  // 1
        value.setInt(a, b);
        System.out.println(Integer.valueOf(temp) == a);
        System.out.println(a == Integer.valueOf(1));
        System.out.println(a.equals(1));
        value.setInt(b, temp);
        
        
    }
    
  
}
