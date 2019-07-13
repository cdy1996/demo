package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advice.AdviceChain;
import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;

import java.lang.reflect.Proxy;
import java.util.function.Supplier;

/**
 * 植入器
 * Created by 陈东一
 * 2019/7/13 0013 13:25
 */
public class JDKProxyFactory implements ProxyFactory {
    
    
    @Override
    public <T> T generateProxy(T target, Advisor advisor) {
        DefaultAdvisor defaultAdvisor = (DefaultAdvisor) advisor;
        if (!((DefaultAdvisor) advisor).matchClass(target)) return target;
    
        Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> {
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
            
                    if (!((DefaultAdvisor) advisor).matchMethod(method)) return method.invoke(target, args);
                
                    Supplier<Object> supplier = () -> {
                        try {
                            return method.invoke(target, args);
                        } catch (Throwable throwable) {
                            return throwable;
                        }
                    };
                    AdviceChain adviceChain = defaultAdvisor.buildChain(supplier);
                
                    return adviceChain.doInvoke(target.getClass(), method, args);
                
                });
        return (T)proxyInstance;
    
    }
    
}
