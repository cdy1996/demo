package com.cdy.demo.framework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by 陈东一
 * 2017/12/28 14:20
 */
@Slf4j
public class CGLibTest {

    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D://tmp");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CGLibTest.class);
        enhancer.setCallback(new methodInterceptorImpl());

        CGLibTest enhancerDemo = (CGLibTest) enhancer.create();
        enhancerDemo.test();

        log.info(enhancerDemo.toString());
    }

    public void test() {
        log.info("enhancer demo thinkInJava");
    }

    private static class methodInterceptorImpl implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            log.info("before invoke " + method);
            Object result = methodProxy.invokeSuper(o, objects);
            log.info("after invoke " + method);
            return result;
        }
    }
}
