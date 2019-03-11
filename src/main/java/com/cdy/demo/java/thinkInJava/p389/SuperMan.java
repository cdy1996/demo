package com.cdy.demo.java.thinkInJava.p389;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈东一 on 2017/9/17 15:38
 */
public class SuperMan {
    class fruit {
    }

    class apple extends fruit {
    }

    class apple1 extends apple {
    }

    static <T> void writeExact(List<T> list, T item) {
        list.add(item);
    }

    static <T> void writeExact1(List<? super T> list, T item) {
        list.add(item);
    }

    static <T> void writeExact2(List<? super apple> list, apple1 item) {
        list.add(item);
    }

    public static void main(String[] args) {
        Man man = new Man();
        Test.hear(man);

        Apples<SuperMan.apple> apples = new Apples<apple>(new SuperMan().new apple());
        Apples<SuperMan.apple> apples1 = new Apples<apple>(new SuperMan().new apple1());


        List superHears = new ArrayList();
        List<?> superHears1 = new ArrayList<apple>();
        List<? extends fruit> superHearss = new ArrayList<apple>();


    }

}

class Fruits<T> {
    public T item;

    Fruits(T item) {
        this.item = item;
    }
}

class Apples<T extends SuperMan.fruit> {
    public T item;

    Apples(T item) {
        this.item = item;
    }
}


interface SuperLook {
    void look();
}

interface SuperHear {
    void hear();
}

class Man implements SuperHear, SuperLook {

    @Override
    public void look() {
        System.out.println("look");
    }

    @Override
    public void hear() {
        System.out.println("hear");
    }
}

class Test {

    public static <T extends SuperLook> void look(T t) {
        t.look();
    }

    public static <T extends SuperHear> void hear(T t) {
        t.hear();
    }
}