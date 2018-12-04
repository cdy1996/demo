package com.hexin.demo.generic;

import java.lang.reflect.Array;

public class Generic {


    public static void main(String[] args) {
        String[] ts = A.getTs(new String());
        Object[] ts2 = A.getTs2(new Object[10]);
        String[] ts3 = A.getTs3(String.class);
    }
}

class A{

    public static <T> T[] getTs(T t){
        System.out.println(t.getClass());
        return (T[]) Array.newInstance(t.getClass(), 10);
    }

    public static <T> T[] getTs2(T[] t){
        System.out.println(t.getClass());
        return (T[]) new Object[10];
    }

    public static <T> T[] getTs3(Class<T> t){
        return (T[]) Array.newInstance(t, 10);
    }
}
