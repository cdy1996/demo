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
    private RemoteDefine remoteDefine;

    public TestProxy(RestTemplate restTemplate, RemoteDefine remoteDefine) {
        this.restTemplate = restTemplate;
        this.remoteDefine = remoteDefine;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return null;
    }
}
