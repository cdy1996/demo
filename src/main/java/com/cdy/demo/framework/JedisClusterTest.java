package com.cdy.demo.framework;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterTest {


    public static void main(String[] args) {
         JedisCluster jedis;
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7000));
            hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7001));
            hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7002));
//            hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7003));
//            hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7004));
//            hostAndPortsSet.add(new HostAndPort("47.98.213.18", 7005));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(1);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(1);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
//        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
//        jedisPoolConfig.setTestOnBorrow(true);
        jedis = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
        jedis.set("Jedis", "Hello Work!");
        System.out.println(jedis.get("Jedis"));
    }

}
