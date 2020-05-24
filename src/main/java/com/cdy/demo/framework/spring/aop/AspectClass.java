package com.cdy.demo.framework.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 如果需要对静态类型字段或者字段 进行 横切, 那么在 aop.xml需要包含那些使用到的类, 不然不会进行增强, 他不是和方法那样只要自己增强就行了
 * <p>
 * https://blog.csdn.net/xichenguan/article/details/86737526
 * <p>
 * Created by 陈东一
 * 2018/10/20 0020 23:00
 */
@Aspect
public class AspectClass {
    
    
    @org.aspectj.lang.annotation.After("set(int com.cdy.demo.framework.spring.aop.Foo.i)")
    public void set() {
        System.out.println("set");
    }
    
    @org.aspectj.lang.annotation.After("get(int com.cdy.demo.framework.spring.aop.Foo.i)")
    public void get() {
        System.out.println("get");
    }
    
    
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
