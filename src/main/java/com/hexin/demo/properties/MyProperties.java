package com.hexin.demo.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "my1.properties")
public class MyProperties {

//    private CustomPropertes custom;

    private Map<String, CustomPropertes> custom = new HashMap<>();

    public Map<String, CustomPropertes> getCustom() {
        return custom;
    }

    public void setCustom(Map<String, CustomPropertes> custom) {
        this.custom = custom;
    }

  /*  public CustomPropertes getCustom() {
        return custom;
    }

    public void setCustom(CustomPropertes custom) {
        this.custom = custom;
    }*/
}
