package com.cdy.demo.framework.mybatis;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by 陈东一
 * 2017/12/30 16:43
 */
@Slf4j
@Data
public class User1 {
    private Integer id;
    private String name;
    
    public User1() {
    }
    
    public User1(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
