package com.cdy.demo.repeatedWheels.mycglib;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;


public class SampleBean {
    private String value;

    public SampleBean() {
    }

    public SampleBean(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public static void main(String[] args) {
        SampleBean sampleBean$ = new SampleBean$(new MethodInterceptorImpl());
        sampleBean$.setValue("123");
        System.out.println(sampleBean$.getValue());
    }

    @Slf4j
    private static class MethodInterceptorImpl implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            log.info("before invoke " + method);
            Object result = methodProxy.invokeSuper(o, objects);
            log.info("after invoke " + method);
            return result;
        }
    }
}