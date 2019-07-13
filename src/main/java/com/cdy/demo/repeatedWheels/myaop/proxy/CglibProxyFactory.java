package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advice.AdviceChain;
import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.function.Supplier;

/**
 * 植入器
 * Created by 陈东一
 * 2019/7/13 0013 13:25
 */
public class CglibProxyFactory implements ProxyFactory{
    
    
    @Override
    public <T>T generateProxy(T target , Advisor advisor){
        DefaultAdvisor defaultAdvisor = (DefaultAdvisor) advisor;
        if (!((DefaultAdvisor) advisor).matchClass(target)) return target;
        
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            String methodName = method.getName();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(target, objects);
            }
            if ("toString".equals(methodName) && parameterTypes.length == 0) {
                return target.toString();
            }
            if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
                return target.hashCode();
            }
            if ("equals".equals(methodName) && parameterTypes.length == 1) {
                return target.equals(objects[0]);
            }
            
//            if (!((DefaultAdvisor) advisor).matchMethod(method)) return methodProxy.invokeSuper(o, objects);
            if (!((DefaultAdvisor) advisor).matchMethod(method)) return method.invoke(target, objects);
            
            Supplier<Object> supplier = () -> {
                try {
//                    return methodProxy.invokeSuper(o, objects);
                    return  method.invoke(target, objects);
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            };
            AdviceChain adviceChain = defaultAdvisor.buildChain(supplier);
    
            return adviceChain.doInvoke(o.getClass(), method, objects);
        });
    
        return (T)enhancer.create();
    }
    
}
