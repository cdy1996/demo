package com.cdy.demo.java.thinkInJava;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by 陈东一 on 2017/9/17 9:04
 */
public class ArrayListTest extends ArrayList<String> {

    public ArrayListTest(int num) {
        for (int i = 0; i < num; i++) {
            this.add("num" + i);
        }
    }

    static final int a = 0;


    public static void main(String[] args) {

        ArrayListTest arrayListTest = new ArrayListTest(5);

        for (String s : arrayListTest) {
            System.out.println(s);
        }

        System.out.println(Array.get(arrayListTest.toArray(), 1));

        Object[] o = (Object[]) Array.newInstance(String.class, 5);
        System.out.println(o.getClass().getSimpleName());


    }

}
