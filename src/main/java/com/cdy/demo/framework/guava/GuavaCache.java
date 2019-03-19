package com.cdy.demo.framework.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCache {

    @Test
    public void testLoadingCache(){
        LoadingCache<String, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000) //最大数量
                .expireAfterWrite(10, TimeUnit.MINUTES) //一个Key一定时间内没有进行更新，那么自动的将其清除，来减小缓存对内存的占用。
                .expireAfterAccess(10, TimeUnit.MINUTES) //一个Key一定时间内没有使用，那么自动的将其清除，来减小缓存对内存的占用。
                .weakKeys()
                .weakValues()
                .softValues()
                .removalListener(notification->{
                    System.out.println(notification.getKey() + ":" + notification.getValue());
                })
                .build(
                        new CacheLoader<String, String>() {
                            public String load(String key) throws Exception {
                                return "null";
                            }
                        });

    }

    @Test
    public void testCallableCache(){
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(); // look Ma, no CacheLoader
            // If the key wasn't in the "easy to compute" group, we need to
            // do things the hard way.
        try {
            cache.get("key", new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "null";
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


}
