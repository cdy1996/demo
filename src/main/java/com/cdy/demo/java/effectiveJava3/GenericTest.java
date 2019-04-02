package com.cdy.demo.java.effectiveJava3;

import java.util.concurrent.ThreadLocalRandom;

public class GenericTest {

//    static <T> T[] toArray(T ...arg){
//        return arg;
//    }

    static <T> T[] toArray(T a, T b){
        Object[] objects = new Object[2];
        objects[0] = a;
        objects[1] = b;
        return (T[])objects;
    }

    static <T> T[] picTow(T a,T b, T c){
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


    public static void main(String[] args) {
//        Object[] attributes = picTow("Good","Fast","Cheap");
        String[] attributes1 = picTow("Good","Fast","Cheap");


    }
}
