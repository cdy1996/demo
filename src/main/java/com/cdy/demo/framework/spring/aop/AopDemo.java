package com.cdy.demo.framework.spring.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * LTW https://www.cnblogs.com/hackem/p/9718311.html
 * Created by 陈东一
 * 2020/5/21 0021 22:37
 */
public class AopDemo {

    /*
    -javaagent:D:\workspace\ideaworkspace\for_action_test\demo\classloaderpath\spring-instrument-5.1.9.RELEASE.jar
     */
    public static void main(String[] args) {
//        aopAnnotationTest();
        aopXmlTest();
    }


    public static void loadTimeWaveTest() {
        System.setProperty("spring.profiles.active", "ltw");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-aop.xml");
        iFoo c1 = (iFoo) applicationContext.getBean("foo");
        c1.fun1();
        applicationContext.stop();
    }

    public static void aopXmlTest() {
        System.setProperty("spring.profiles.active", "xml");

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-aop.xml");
        iFoo c1 = (iFoo) applicationContext.getBean("foo");
        c1.fun1();
        applicationContext.stop();
    }

    public static void aopAnnotationTest() {
        System.setProperty("spring.profiles.active", "annotation");

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext-aop.xml");
        iFoo c1 = (iFoo) applicationContext.getBean("foo");
        c1.fun1();
        applicationContext.stop();
    }
}
