package com.cdy.demo.repeatedWheels.myaop.proxy;

import com.cdy.demo.repeatedWheels.myaop.advisor.Advisor;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;
import javassist.util.proxy.ProxyObject;

/**
 * 植入器
 * Created by 陈东一
 * 2019/7/13 0013 13:25
 */
public class JavassistProxyFactory implements ProxyFactory {
    
    
    @Override
    public <T> T generateProxy(T target, Advisor advisor) {
        DefaultAdvisor defaultAdvisor = (DefaultAdvisor) advisor;
        if (!((DefaultAdvisor) advisor).matchClass(target)) return target;
        
        
        javassist.util.proxy.ProxyFactory factory = new javassist.util.proxy.ProxyFactory();
        factory.setInterfaces(target.getClass().getInterfaces());
        Class<?> proxyClass = factory.createClass();
        ProxyObject javassistProxy;
        try {
            javassistProxy = (ProxyObject) proxyClass.newInstance();
            javassistProxy.setHandler((self, method, proceed, args) ->
                    doInterceptor(target, (DefaultAdvisor) advisor, method, args));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (T) javassistProxy;
    }
    
}
