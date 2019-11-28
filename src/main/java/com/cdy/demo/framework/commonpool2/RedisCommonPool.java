package com.cdy.demo.framework.commonpool2;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.concurrent.atomic.AtomicInteger;

public class RedisCommonPool {

    public static class JedisFactory extends BasePooledObjectFactory<Jedis> {

        private String host = System.getProperty("host");
        private String port = System.getProperty("port");
        private String timeout = System.getProperty("timeout");
        private String auth = System.getProperty("auth");

        private AtomicInteger integer = new AtomicInteger();

        @Override
        public Jedis create() throws Exception {
            Jedis jedis = new Jedis(host, Integer.parseInt(port), Integer.parseInt(timeout==null?"3000":timeout));
            jedis.connect();
            if (auth != null) {
                jedis.auth(auth);
            }
            return jedis;
        }

        @Override
        public boolean validateObject(PooledObject<Jedis> p) {
            return p.getObject().ping().equals("PONG");
        }

        @Override
        public void destroyObject(PooledObject<Jedis> p) throws Exception {
            p.getObject().quit();
        }

        @Override
        public PooledObject<Jedis> wrap(Jedis conn) {
            //包装实际对象
            return new DefaultPooledObject<>(conn);
        }
    }

    public static class JedisPool extends GenericObjectPool<Jedis> {

        public JedisPool(PooledObjectFactory<Jedis> factory) {
            super(factory);
        }

        public JedisPool(PooledObjectFactory<Jedis> factory, GenericObjectPoolConfig config) {
            super(factory, config);
        }

        public JedisPool(PooledObjectFactory<Jedis> factory, GenericObjectPoolConfig config, AbandonedConfig abandonedConfig) {
            super(factory, config, abandonedConfig);
        }
    }

    public static void main(String[] args) throws Exception{
        JedisFactory orderFactory = new JedisFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(5);
        //设置获取连接超时时间
        config.setMaxWaitMillis(1000);
        JedisPool connectionPool = new JedisPool(orderFactory, config);
        for (int i = 0; i < 7; i++) {
            Jedis o = connectionPool.borrowObject();
            System.out.println("brrow a connection: " + o +" active connection:"+connectionPool.getNumActive());
            connectionPool.returnObject(o);
        }
    }
}