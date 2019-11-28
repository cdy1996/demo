package com.cdy.demo.framework.commonpool2;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcCommonPool {

    public static class ConnectionFactory extends BasePooledObjectFactory<Connection> {

        private String url = System.getProperty("jdbcUrl");
        private String username = System.getProperty("username");
        private String password = System.getProperty("password");


        @Override
        public Connection create() throws Exception {
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public PooledObject<Connection> wrap(Connection conn) {
            //包装实际对象
            return new DefaultPooledObject<>(conn);
        }
    }

    public static class ConnectionPool extends GenericObjectPool<Connection> {

        public ConnectionPool(PooledObjectFactory<Connection> factory) {
            super(factory);
        }

        public ConnectionPool(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig config) {
            super(factory, config);
        }

        public ConnectionPool(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
            super(factory, config, abandonedConfig);
        }
    }

    public static void main(String[] args) throws Exception{
        ConnectionFactory orderFactory = new ConnectionFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5);
        //设置获取连接超时时间
        config.setMaxWaitMillis(1000);
        ConnectionPool connectionPool = new ConnectionPool(orderFactory, config);
        for (int i = 0; i < 7; i++) {
            Connection o = connectionPool.borrowObject();
            System.out.println("brrow a connection: " + o +" active connection:"+connectionPool.getNumActive());
            connectionPool.returnObject(o);
        }
    }
}