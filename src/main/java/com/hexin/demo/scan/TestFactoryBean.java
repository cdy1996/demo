package com.hexin.demo.scan;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

public class TestFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private final Class<T> testInterface;
    private TestDefine testDefine;
    private ApplicationContext applicationContext;

    public TestFactoryBean(Class<T> testInterface, TestDefine testDefine) {
        this.testInterface = testInterface;
        this.testDefine = testDefine;
    }

    @Override
    public T getObject() throws Exception {
        TestConfiguration testConfiguration;
        if(StringUtils.isNotBlank(testDefine.getConfiguration())){
            testConfiguration = applicationContext.getBean(testDefine.getConfiguration(), TestConfiguration.class);
        } else {
            testConfiguration = applicationContext.getBean(TestConfiguration.class);
        }

        RestTemplate restTemplate;
        if(testConfiguration == null || StringUtils.isBlank(testConfiguration.getTemplateName())){
            restTemplate = applicationContext.getBean(RestTemplate.class);
        } else {
            restTemplate = applicationContext.getBean(testConfiguration.getTemplateName(), RestTemplate.class);
        }

        TestProxy testProxy = new TestProxy(restTemplate, testDefine);
        T t = (T) Proxy.newProxyInstance(this.testInterface.getClassLoader(), new Class[]{this.testInterface}, testProxy);
        return t;
    }

    @Override
    public Class<?> getObjectType() {
        return this.testInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
