package com.cdy.demo.framework.mybatis;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * todo 描述
 * Created by 陈东一
 * 2019/1/8 0008 22:00
 */
public interface UserDao {
    
    
    @Select("select * from user where id = #{id}")
    @ResultType(User1.class)
    User1 getBy(@Param("id") Integer id);
}
