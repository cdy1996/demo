package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

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
        // methodProxy.invokeSuper(o, objects);
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) ->
                doInterceptor(target, (DefaultAdvisor) advisor, method, args));
    
        return (T)enhancer.create();
    }
    
}
