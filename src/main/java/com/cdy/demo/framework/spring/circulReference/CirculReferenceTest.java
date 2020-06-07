package com.cdy.demo.framework.spring.circulReference;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.statemachine.config.common.annotation.EnableAnnotationConfiguration;

/**
 * 特殊的循环依赖测试
 * Created by 陈东一
 * 2020/5/21 0021 22:08
 */
@Configuration
@EnableAspectJAutoProxy
@Import(value = {C1.class, C2.class, AspectClass.class})
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
