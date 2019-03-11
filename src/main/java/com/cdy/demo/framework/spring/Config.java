package com.cdy.demo.framework.spring;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by viruser on 2018/8/1.
 */
//@Configuration
//@PropertySource("classpath:cd.properties")
//@ComponentScan(basePackages = {"spring"})
public class Config {

    @Value("${title}")
    public String title;
}
