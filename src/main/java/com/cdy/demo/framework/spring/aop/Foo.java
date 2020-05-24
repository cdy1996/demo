package com.cdy.demo.framework.spring.aop;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Foo implements iFoo {
    public static List<String> list = new ArrayList<>();
    public int i = 0;
    
    public void fun1() {
        System.out.println("外部方法" + i);
        i = 2; //对字段的 aop
        fun2();
    }
    
    public void fun2() {
        System.out.println("内部方法");
    }
}
