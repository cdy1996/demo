package com.cdy.demo.framework.spring.aop;

import org.springframework.stereotype.Component;

@Component
public class Foo implements iFoo {
    public void fun1() {
        System.out.println("外部方法");
//        fun2();
    }
    
    public void fun2() {
        System.out.println("内部方法");
    }
}
