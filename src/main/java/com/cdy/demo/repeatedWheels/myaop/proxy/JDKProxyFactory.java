package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;

import java.lang.reflect.Proxy;

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
                (proxy, method, args) -> doInterceptor(target, (DefaultAdvisor) advisor, method, args));
        return (T)proxyInstance;
    
    }
    
   
}
