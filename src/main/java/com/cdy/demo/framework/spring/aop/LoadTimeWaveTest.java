package com.cdy.demo.framework.spring.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * LTW https://www.cnblogs.com/hackem/p/9718311.html
 * Created by 陈东一
 * 2020/5/21 0021 22:37
 */
public class LoadTimeWaveTest {
    
    /*
    -javaagent:D:\workspace\ideaworkspace\for_action_test\demo\classloaderpath\spring-instrument-5.1.9.RELEASE.jar
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-ltw.xml");
        Foo c1 = (Foo) applicationContext.getBean("foo");
        c1.fun1();
        applicationContext.stop();
    }
}
