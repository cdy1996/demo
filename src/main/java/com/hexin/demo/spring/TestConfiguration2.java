package com.hexin.demo.spring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestConfiguration2 {


    @Bean
    public User user2(){
        System.out.println("user2");
        return new User();
    }


    @Bean
    public User user3(){
        System.out.println("user3");
        return new User();
    }
}
