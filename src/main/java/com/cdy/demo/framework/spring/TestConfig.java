package com.cdy.demo.framework.spring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//@ComponentScan("com.hexin.demo.spring")
//@Component("TestConfig")
//@Import(value = {/*TestConfiguration2.class,*/TestConfiguration.class})
public class TestConfig {

    public TestConfig() {
        System.out.println("reactor1");
    }


    @Configuration
    class A{

    }

    @Configuration
    static class B{

    }

    @Bean
    public User user1(){
        return new User();
    }

    @Bean
    public static User user2(){
        return new User();
    }

}
