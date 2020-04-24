package com.cdy.demo.framework.mybatis.plugin;

/**
 * Created by 陈东一
 * 2018/1/8 15:46
 */
public class ServiceImpl implements IService {
    
    @Override
    public void service(String name) {
        System.out.println("Hello " + name);
    }
    
}