package com.hexin.demo.spring;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TestConfiguration {

//
//    @Bean
//    public User user(){
//        System.out.println("user");
//        return new User();
//    }
//
//
//    @Bean
//    public User user1(){
//        System.out.println("user1");
//        return new User();
//    }


    @Bean
    public User user(){
        User user = new User();
        user.setUsername("1");
        return user;
    }

//    @Bean
//    public User user4(){
//        return new User4();
//    }

    @Bean
    public User4 user4(){
        User4 user4 = new User4();
        user4.setUsername("4");
        return user4;
    }

    @Bean
    public User44 user44(){
        User44 user44 = new User44();
        user44.setUsername("44");
        return user44;
    }


}
