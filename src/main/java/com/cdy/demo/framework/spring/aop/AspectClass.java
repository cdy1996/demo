package com.cdy.demo.framework.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by 陈东一
 * 2018/10/20 0020 23:00
 */
@Aspect
public class AspectClass {
    
    @Pointcut("execution(public * com.cdy.demo.framework.spring.aop.*.*(..))")
    public void before() {
    }
    
    @Around("before()")
    public Object myAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("before");
        Object obj = joinPoint.proceed();
        System.out.println("after");
        return obj;
    }
    
}
