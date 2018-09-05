package com.hexin.demo.spring;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
@ContextConfiguration(classes = TestConfig.class) //加载配置文件
public class Test {


    User user44;
    @Autowired
    public void setUser44(User user44) {
        this.user44 = user44;
    }

    @org.junit.Test
    public void test(){
        System.out.println(user44.getUsername());
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
//        annotationConfigApplicationContext.scan("com.hexin.demo.spring");
//        annotationConfigApplicationContext.refresh();


        User user = (User) applicationContext.getBean(User.class);
//        User user4 = (User) applicationContext.getBean("user4");
//        User user44 = (User) applicationContext.getBean("user44");
        System.out.println(user);
//        System.out.println(user4);
//        System.out.println(user44);

    }
}
