package com.hexin.demo.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by viruser on 2018/8/1.
 */
@Configuration
@PropertySource("classpath:cd.properties")
@ComponentScan(basePackages = {"spring"})
public class Config {

    @Value("${title}")
    public String title;
}
