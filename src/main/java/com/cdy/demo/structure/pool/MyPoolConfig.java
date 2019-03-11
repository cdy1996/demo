package com.cdy.demo.structure.pool;


import lombok.Data;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Properties;

/**
 * 自定义实现数据库
 * Created by 陈东一
 * 2018/3/4 20:40
 */
@Data
public class MyPoolConfig {
    private String dirver;
    private String url;
    private String user;
    private String password;
    private int initSize;
    private int maxSize;
    private int poolSize;
    private String test;
    private boolean chexk;
    private long timeout;
    
    public static class Singleton {
        private static final MyPoolConfig POOL_CONFIG = new MyPoolConfig();

        private Singleton() {
        }
        
        public static final MyPoolConfig getInstance() {
            return POOL_CONFIG;
        }
    }
    
    private MyPoolConfig() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void init() throws Exception {
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource("my.properties");
        FileInputStream fis = new FileInputStream(url.getFile());
        properties.load(fis);

        for (Object o : properties.keySet()) {
            String name = o.toString().replace("gp.", "");
            Field field = this.getClass().getDeclaredField(name);

            Method method = this.getClass().getMethod(toUpper(name), field.getType());
            Class<?> type = field.getType();
            System.out.println(properties.get(o).toString());
            if (type.isAssignableFrom(long.class)) {
                method.invoke(this, Long.valueOf(properties.get(o).toString()));
            } else if (type.isAssignableFrom(int.class)) {
                method.invoke(this, Integer.valueOf(properties.get(o).toString()));
            } else if (type.isAssignableFrom(boolean.class)) {
                method.invoke(this, Boolean.valueOf(properties.get(o).toString()));
            } else {
                method.invoke(this, properties.get(o));
            }
            
        }
        
        
    }
    
    private String toUpper(String name) {
        char[] chars = name.toCharArray();
        chars[0] -= 32;
        return "set" + new String(chars);
    }
    
    
}
