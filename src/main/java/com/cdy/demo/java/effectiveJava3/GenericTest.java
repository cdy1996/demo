package com.cdy.demo.java.effectiveJava3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 第32条
 */
public class GenericTest {

    static <T> T[] toArray(T ...arg){
        System.out.println(arg.getClass());
        System.out.println(arg[0].getClass());
        return arg;
    }
    
    static <T> List<T> toList(T ...arg){
        System.out.println(arg.getClass());
        System.out.println(arg[0].getClass());
        List<T> list = new ArrayList<>();
        for (T t : arg) {
            list.add(t);
        }
        return list;
    }

//    static <T> T[] toArray(T a, T b){
//        Object[] objects = new Object[2];
//        objects[0] = a;
//        objects[1] = b;
//        return (T[])objects;
//    }

    static <T> T[] picTow(T a,T b, T c){
        System.out.println(a.getClass());
        switch(ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                return toArray(a, b);
            case 1:
                return toArray(a, c);
            case 2:
                return toArray(b, c);
        }
        throw new AssertionError();
    }
    
    static <T> List<T> picTow2(T a,T b, T c){
        switch(ThreadLocalRandom.current().nextInt(3)) {
            case 0:
                return toList(a, b);
            case 1:
                return toList(a, c);
            case 2:
                return toList(b, c);
        }
        throw new AssertionError();
    }
    


    public static void main(String[] args) {
        Object[] attributes = picTow("Good","Fast","Cheap");
        System.out.println(attributes[0].getClass());
//        String[] attributes1 = picTow("Good","Fast","Cheap");
//        List<String> strings = picTow2("Good", "Fast", "Cheap");
    
//        List<String> strings = toList("Good", "Fast", "Cheap");
//        String[] strings1 = toArray("Good", "Fast", "Cheap");
    }
    
    static void dangerous(List<String>... stringList) {
        List<Integer> integerList = Arrays.asList(42);
//        stringList[0] = integerList;
        Object[] object  = stringList;
        object[0] = integerList;
        String s = stringList[0].get(0);
    }
    
}
