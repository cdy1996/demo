package com.hexin.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class CustomPropertes {

    private String custom;

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }
}
