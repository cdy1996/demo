package com.cdy.demo.framework.spring.circulReference;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 循环依赖 +  aop代理
 * 即 a ->  b -> a(代理后) -> b
 * 这里的a 一开始只是暴露 earlybean 还没有被代理,  但是spring通过singleFactory
 * 提供了 getEarlyBeanReference 方法来弥补a 没有走到 初始化后期 的 生成aop代理
 * Created by 陈东一
 * 2018/10/20 0020 23:00
 */
@Aspect
@Component
public class AspectClass {
    
    
    @Pointcut("execution(public * com.cdy.demo.framework.spring.circulReference.*.*(..))")
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
