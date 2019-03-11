package com.cdy.demo.framework.mybatis;

public interface User1Dao {
    
    User1 selectOne(Integer id);
    
    Boolean insertOne(Integer id, String name);
    
    
}
