package com.cdy.demo.framework.spring.aop;

import org.aspectj.lang.annotation.Aspect;

/**
 * Created by 陈东一
 * 2018/10/20 0020 23:00
 */
@Aspect
public class AspectClass {
        
        //    @Pointcut()
        @org.aspectj.lang.annotation.Before("execution(* fun1())")
        public void before() {
            System.out.println("before");
        }
    
}
