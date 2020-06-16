package com.cdy.demo.middleware.redis.redission;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LockDemo {

    public static void main(String[] args) throws InterruptedException {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://47.98.213.18:7000");
        RedissonClient redissonClient = Redisson.create(config);
        RLock lock = redissonClient.getLock("123");
//        publish(redissonClient);

        Thread thread = new Thread(() -> {
            lock.lock(1000, TimeUnit.SECONDS);

            System.out.println(new Date());

            lock.unlock();
        }, "lock-1");
        Thread thread2 = new Thread(() -> {
            lock.lock(1000, TimeUnit.SECONDS);

            System.out.println(new Date());

            lock.unlock();
        }, "lock-2");

        thread.start();
        thread2.start();

        thread2.join();
        thread.join();
    }

    private static void publish(RedissonClient redissonClient) {
        RTopic topic = redissonClient.getTopic("!23");
        topic.publish("123");
    }

    public static void test() {
        RedissonReactiveClient reactive = Redisson.createReactive();
        RMapReactive<Object, Object> map = reactive.getMap("!23");
//        map.get("!23").subscribe()
    }
}
