package com.cdy.demo.repeatedWheels.myaop.advice;

import java.lang.reflect.Method;

/**
 * 后置接口
 * Created by 陈东一
 * 2019/7/13 0013 13:23
 */
public interface AfterAdvice extends Advice {
    
    
    <T> Object invokeAfter(Class<T> clazz, Method method, Object[] args, Object result, Exception exception);
}
