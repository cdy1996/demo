package com.cdy.demo.framework.commonpool2;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCommonPool {

    public static class ThreadFactory extends BasePooledObjectFactory<Thread> {

        private String name = System.getProperty("threadName", "pool-");

        private AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread create() throws Exception {
            return new Thread(name + integer.getAndIncrement());
        }

        @Override
        public void destroyObject(PooledObject<Thread> p) throws Exception {
            p.getObject().interrupt();
        }

        @Override
        public boolean validateObject(PooledObject<Thread> p) {
            return p.getObject().isAlive();
        }

        @Override
        public PooledObject<Thread> wrap(Thread conn) {
            //包装实际对象
            return new DefaultPooledObject<>(conn);
        }
    }

    public static class ThreadPool extends GenericObjectPool<Thread> {

        public ThreadPool(PooledObjectFactory<Thread> factory) {
            super(factory);
        }

        public ThreadPool(PooledObjectFactory<Thread> factory, GenericObjectPoolConfig config) {
            super(factory, config);
        }

        public ThreadPool(PooledObjectFactory<Thread> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
            super(factory, config, abandonedConfig);
        }
    }

    public static void main(String[] args) throws Exception{
        ThreadFactory orderFactory = new ThreadFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5);
        //设置获取连接超时时间
        config.setMaxWaitMillis(1000);
        ThreadPool connectionPool = new ThreadPool(orderFactory, config);
        for (int i = 0; i < 7; i++) {
            Thread o = connectionPool.borrowObject();
            System.out.println("brrow a connection: " + o +" active connection:"+connectionPool.getNumActive());
            connectionPool.returnObject(o);
        }
    }
}