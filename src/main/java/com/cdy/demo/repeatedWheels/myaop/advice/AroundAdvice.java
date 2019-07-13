package com.cdy.demo.repeatedWheels.myaop.advice;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 环绕接口
 * Created by 陈东一
 * 2019/7/13 0013 13:23
 */
public interface AroundAdvice extends Advice{
    
    <T>Object invoke(Class<T> clazz, Method method, Object[] args, Supplier<Object> supplier) throws Exception;
}
