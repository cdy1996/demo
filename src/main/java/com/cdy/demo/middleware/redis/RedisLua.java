package com.cdy.demo.middleware.redis;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * todo
 * Created by 陈东一
 * 2018/12/2 0002 18:13
 */
public class RedisLua {
    private static final  String lua =
            "local num = redis.call('incr' , KEYS[1])\n" +
            "if tonumber(num)==1 then\n" +
            "\tredis.call('expire', KEYS[1] , ARGV[1])\n" +
            "\treturn 1\n" +
            "elseif tonumber(num)>tonumber(ARGV[2]) then\n" +
            "\treturn 0\n" +
            "else \n" +
            "\treturn 1\n" +
            "end";

    public static void main(String[] args) {
        Jedis jedis = RedisManager.getJedis();
        List<String> keys = new ArrayList<>();
        keys.add("ip:limit:127.0.0.1");
        List<String> values = new ArrayList<>();
        values.add("6000");
        values.add("10");

        String s = jedis.scriptLoad(lua);
        Object ad = jedis.evalsha(s, keys, values);
        System.out.println(ad);
//
        Object eval = jedis.eval(lua, keys, values);
        System.out.println(eval);

    }


}
