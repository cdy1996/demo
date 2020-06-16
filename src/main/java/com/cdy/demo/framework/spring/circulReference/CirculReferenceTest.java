package com.cdy.demo.framework.spring.circulReference;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.config.common.annotation.EnableAnnotationConfiguration;

/**
 * 特殊的循环依赖测试
 * 被aop循环代理的对象进行 循环依赖  -- 会SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference 获取提前代理的对象
 * factoryBean的循环依赖  -- factorybean 如果里面有依赖注入的话, 在getObject时可能会存在异常
 * Created by 陈东一
 * 2020/5/21 0021 22:08
 */
@Configuration
@EnableAspectJAutoProxy
@Import(value = {C1.class, C2.class, AspectClass.class})
//@Import(value = {C1Factory.class, C2Factory.class})
@EnableAnnotationConfiguration
public class CirculReferenceTest {
    
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(CirculReferenceTest.class);
    
        Object c1 = annotationConfigApplicationContext.getBean("c1");
    
        annotationConfigApplicationContext.getEnvironment().getProperty("abc");
        annotationConfigApplicationContext.getEnvironment().resolvePlaceholders("abc");
    
        annotationConfigApplicationContext.stop();
    }
}
