package com.cdy.demo.framework;

import com.cdy.demo.repeatedWheels.mycglib.SampleBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 陈东一
 * 2017/12/28 14:20
 */
@Slf4j
public class CGLibTest {

    public static void main(String[] args) throws InvocationTargetException {

//        testAOP();
        testFastMethod();
    }
    static void testAOP(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/classes");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleBean.class);
        enhancer.setCallback(new methodInterceptorImpl());

        SampleBean enhancerDemo = (SampleBean) enhancer.create();
        enhancerDemo.setValue("123");

        log.info(enhancerDemo.getValue());
    }

    static void testFastMethod() throws InvocationTargetException {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/classes");
        FastClass fastClass = FastClass.create(CGLibTest.class);
        FastMethod fastMethod = fastClass.getMethod("test", new Class[0]);
        CGLibTest bean = new CGLibTest();
        fastMethod.invoke(bean, new Object[0]);
    }



    public void test() {
        test2();
    }

    public void test2() {
        log.info("内部方法");
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
