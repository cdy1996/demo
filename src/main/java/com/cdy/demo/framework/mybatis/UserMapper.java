package com.cdy.demo.framework.mybatis;

public interface UserMapper {
    
    User1 selectOne(Integer id);
    
    Boolean insertOne(Integer id, String name);
    
    
}
