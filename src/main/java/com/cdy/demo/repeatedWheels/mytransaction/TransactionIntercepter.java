package com.cdy.demo.repeatedWheels.mytransaction;

import com.cdy.demo.repeatedWheels.myaop.advice.AroundAdvice;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * todo
 * Created by 陈东一
 * 2019/7/13 0013 16:40
 */
public class TransactionIntercepter implements AroundAdvice {
    ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    
    public Connection getConnect() throws SQLException {
        Connection connection = threadLocal.get();
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test");
            connection.setAutoCommit(false);
            threadLocal.set(connection);
        }
        return connection;
    }
    
    public void commit() throws SQLException {
        Connection connection = threadLocal.get();
        connection.commit();
        threadLocal.remove();
    }
    
    public void rollback() throws SQLException {
        Connection connection = threadLocal.get();
        connection.rollback();
        threadLocal.remove();
    }
    
    @Override
    public <T> Object invoke(Class<T> clazz, Method method, Object[] args, Supplier<Object> supplier) throws Exception {
        getConnect();
        Object result = null;
        try {
            result = supplier.get();
        } catch (Exception e) {
            rollback();
        }
        commit();
        return result;
    }
}
