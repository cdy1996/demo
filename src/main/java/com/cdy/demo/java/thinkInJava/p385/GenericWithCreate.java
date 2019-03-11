package com.cdy.demo.java.thinkInJava.p385;

/**
 * Created by 陈东一 on 2017/9/17 11:46
 */
public abstract class GenericWithCreate<T> {
    final T elemnt;

    GenericWithCreate() {
        elemnt = create();
        System.out.println("GenericWithCreate构造方法");
    }

    abstract T create();
}

class X {
}

class Creator extends GenericWithCreate<X> {
    @Override
    X create() {
        System.out.println("Creator.create()");
        return new X();
    }

    void f() {
        System.out.println(elemnt.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        Creator c = new Creator();
        c.f();

    }
}