package com.cdy.demo.repeatedWheels.myaop.advice;

import java.lang.reflect.Method;

/**
 * 前置接口
 * Created by 陈东一
 * 2019/7/13 0013 13:23
 */
public interface BeforeAdvice extends Advice{
    
    <T> void invokeBefore(Class<T> clazz, Method method, Object[] args);
}
