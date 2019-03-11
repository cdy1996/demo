package com.cdy.demo.java.thinkInJava;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by 陈东一 on 2017/9/16 16:44
 */
public class Mytest {
    /*Runnable runnable = ()->System.out.println("123");*/

    static <T> T getNum(T t) {
        return t;
    }

    public static void main(String[] args) {

        Mytest.<Integer>getNum(123);

        //System.out.println(1/0);
        /*	Runnable runnable = ()->System.out.println("123");*/

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "123");
        map.put("2", "1234");
        System.out.println(map.get("1"));

        byte[] b = {-26, -75, -73};
        ByteBuffer bb = ByteBuffer.allocate(3);
        bb.put(b, 0, 3);
        bb.flip();
        CharBuffer cb = UTF_8.decode(bb);
        char c = cb.charAt(0);
        System.out.println(c);
    }
}

abstract class A {
    int a;

    A(int a) {
        this.a = a;
    }

    public abstract void oa();
}