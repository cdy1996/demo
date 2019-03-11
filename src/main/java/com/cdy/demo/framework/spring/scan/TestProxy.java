package com.cdy.demo.framework.spring.scan;

import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestProxy implements InvocationHandler {

    private static Map<Class<? extends Annotation>, AnnotationProcess> map = new HashMap<>();

    static {

    }

    private RestTemplate restTemplate;
    private TestDefine testDefine;

    public TestProxy(RestTemplate restTemplate, TestDefine testDefine) {
        this.restTemplate = restTemplate;
        this.testDefine = testDefine;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}
