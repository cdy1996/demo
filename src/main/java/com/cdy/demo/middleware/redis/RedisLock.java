package com.cdy.demo.middleware.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;

/**
 * todo
 * Created by 陈东一
 * 2018/12/2 0002 16:16
 */
public class RedisLock {

    public String getLock(String key, int timeout){
        Jedis jedis = RedisManager.getJedis();
        String value = UUID.randomUUID().toString();
        long end = System.currentTimeMillis() +timeout;
        while (System.currentTimeMillis() < end) {
            if (jedis.setnx(key, value) == 1) {
                jedis.expire(key, timeout);
//                jedis.close();
                return value;
            }
            if (jedis.ttl(key) == -1) {
                jedis.expire(key, timeout);
            }
        }
        jedis.close();
        return null;
    }

    public boolean releaseLock(String key, String value){
        Jedis jedis = RedisManager.getJedis();
        while(true) {
            jedis.watch(key);  //一旦key被修改或者删除, 后面的事务代码就不会执行  //方式其他地方对key的修改来对释放所造成影响
            if (value.equals(jedis.get(key))) {
                Transaction transaction = jedis.multi();
                transaction.del(key);
                List<Object> list = transaction.exec();
                if (list == null) {
                    continue;
                }


            }
            jedis.unwatch();
            break;
        }
//        jedis.close();
        return false;
    }

    public static void main(String[] args) {
        RedisLock lock = new RedisLock();
        String lock1 = lock.getLock("123", 1000);
        System.out.println(lock1);

    }

}
