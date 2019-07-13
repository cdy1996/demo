package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advice.AdviceChain;
import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * 植入器
 * Created by 陈东一
 * 2019/7/13 0013 13:25
 */
public interface ProxyFactory {
    <T> T generateProxy(T target, Advisor advisor);
    
    default  <T> Object doInterceptor(T target, DefaultAdvisor advisor, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(target, args);
        }
        if ("toString".equals(methodName) && parameterTypes.length == 0) {
            return target.toString();
        }
        if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
            return target.hashCode();
        }
        if ("equals".equals(methodName) && parameterTypes.length == 1) {
            return target.equals(args[0]);
        }
        
        if (!advisor.matchMethod(method)) return method.invoke(target, args);
        
        Supplier<Object> supplier = () -> {
            try {
                return method.invoke(target, args);
            } catch (Throwable throwable) {
                return new RuntimeException(throwable);
            }
        };
        AdviceChain adviceChain = advisor.buildChain(supplier);
        
        return adviceChain.doInvoke(target.getClass(), method, args);
    }
    
}
