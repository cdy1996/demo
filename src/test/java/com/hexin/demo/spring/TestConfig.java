package com.hexin.demo.spring;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
//@Component("com.hexin.demo.spring.TestConfig")
@Import(value = {/*TestConfiguration2.class,*/TestConfiguration.class})
public class TestConfig {



}
