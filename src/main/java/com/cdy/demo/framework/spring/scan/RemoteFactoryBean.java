package com.cdy.demo.framework.spring.scan;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

public class RemoteFactoryBean<T> implements FactoryBean<T>, ApplicationContextAware {

    private final Class<T> testInterface;
    private RemoteDefine remoteDefine;
    private ApplicationContext applicationContext;

    public RemoteFactoryBean(Class<T> testInterface, RemoteDefine remoteDefine) {
        this.testInterface = testInterface;
        this.remoteDefine = remoteDefine;
    }

    @Override
    public T getObject() throws Exception {
        TestConfiguration testConfiguration;
        if(StringUtils.isNotBlank(remoteDefine.getConfiguration())){
            testConfiguration = applicationContext.getBean(remoteDefine.getConfiguration(), TestConfiguration.class);
        } else {
            testConfiguration = applicationContext.getBean(TestConfiguration.class);
        }

        RestTemplate restTemplate;
        if(testConfiguration == null || StringUtils.isBlank(testConfiguration.getTemplateName())){
            restTemplate = applicationContext.getBean(RestTemplate.class);
        } else {
            restTemplate = applicationContext.getBean(testConfiguration.getTemplateName(), RestTemplate.class);
        }

        TestProxy testProxy = new TestProxy(restTemplate, remoteDefine);
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
