package com.cdy.demo.java.generic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 协变逆变测试
 *
 * 如果某个返回的类型可以由其派生类型替换，那么这个类型就是支持协变的
 * 如果某个参数类型可以由其基类替换，那么这个类型就是支持逆变的。
 */
public class Generic2 {

    //协变 返回了子类
    public C test(){
        return new CC();
    }


    @Test
    public void test1(){
        test1(new C());
        test1(new CC());

        test11(new C());
        test11(new CC());

        //逆变
        test111(new D<C>());
        test111(new D<CC>());

        //协变
        test1111(new D<C>());
        test1111(new D<CC>());



    }

    public void test1(C c){
    }

    public <T extends C> void test11(T t){

    }

    public void test111(D<? super CC> d){

    }

    public void test1111(D<? extends C> d){
    }

    @Test
    public void test2(){
        //协变
        //
        List<? extends C> list =  new ArrayList<CC>();
        //支持协变(out)的类型参数只能用在输出位置：函数返回值、属性的get访问器以及委托参数的某些位置
        C c = list.get(1);
        list.add(null);
//        list.add(new CC());
//        list.add(new C());
        //逆变
        List<? super CC> list2 =  new ArrayList<C>();
        //支持逆变(in)的类型参数只能用在输入位置：方法参数或委托参数的某些位置中出现。
        list2.add(null);
        list2.add(new CC());
//        list2.add(new Object());
        Object object = list2.get(1);
//        list2.add(new C());
    }



}

class C{}
class CC extends C{}

class D<E>{}
class DD<E> extends D{};
