package com.cdy.demo.framework.spring.spel;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration("config")
@PropertySource("abc.properties")
public class TestConfig {
    String title = "123";

}
