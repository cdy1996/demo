package com.cdy.demo.middleware.redis.redission;

import org.redisson.Redisson;
import org.redisson.api.*;

import java.util.concurrent.TimeUnit;

public class LockDemo {

    public static void main(String[] args) {
        RedissonClient redissonClient = Redisson.create();
        RLock lock = redissonClient
                .getLock("123");
        RTopic topic = redissonClient.getTopic("!23");
        topic.publish("123");

        lock.lock(10, TimeUnit.SECONDS);


        lock.unlock();
    }

    public static void test() {
        RedissonReactiveClient reactive = Redisson.createReactive();
        RMapReactive<Object, Object> map = reactive.getMap("!23");
//        map.get("!23").subscribe()
    }
}
