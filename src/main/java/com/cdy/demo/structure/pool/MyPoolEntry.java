package com.cdy.demo.structure.pool;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Connection;

/**
 * 自定义实现数据库
 * Created by 陈东一
 * 2018/3/4 22:08
 */
@Data
@AllArgsConstructor
public class MyPoolEntry {
    private Connection connection;
    
    private long useStartTIme;
    

}
