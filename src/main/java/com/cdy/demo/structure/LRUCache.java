package com.cdy.demo.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LRU todo
 * Created by 陈东一
 * 2019/3/13 0013 23:07
 */
public class LRUCache {

    ConcurrentHashMap<String, Cache> cache = new ConcurrentHashMap<>();
    ReentrantLock lock = new ReentrantLock();

//    Set<String> lruQueue = new LinkedHashSet<String>();

    List<String> lruQueue = new LinkedList<>();

    int size = 1000;


    /**
     * 存入缓存
     * @param key
     * @param value
     * @param timeout
     * @return 如果key存在则返回原来的值(null则说明过期了),否则返回value
     */
    public Object put(String key, Object value, long timeout){
        lock.lock();
        Object result = null;
        try {
            Cache cache = new Cache(value, System.currentTimeMillis() + timeout);
            Cache o = this.cache.get(key);
            if (o != null) {
                //超时
                if (o.timeout > System.currentTimeMillis()) {
                    o.data=null;
                    this.cache.put(key, cache);
                } else{
                    result = o.data;
                    this.cache.put(key, cache);
                }
            } else {
                result = value;
                this.cache.put(key, cache);
                lruQueue.add(key);
            }
            if (this.cache.size() > size) {
                release();
            }
            return result;
        } finally {
            lock.unlock();
        }
    }

    public Object get(String key) {
        lock.lock();
        Object result;
        try {
            Cache o = this.cache.get(key);
            if (o != null) {
                //超时
                if (o.timeout > System.currentTimeMillis()) {
                    this.cache.remove(key);
                    lruQueue.remove(key);
                    result = null;
                } else{
                    result = o.data;
                    lruQueue.add(key);
                }
            } else {
                result = null;
            }
            if (this.cache.size() > size) {
                release();
            }
            return result;
        } finally {
            lock.unlock();
        }

    }

    public void releaseTimeout(){
        long now = System.currentTimeMillis();
        lruQueue.removeIf(key->{
            Cache cache = this.cache.get(key);
            if (cache.timeout < now) {
                this.cache.remove(key);
                return true;
            }
            return false;
        });
    }

    public void release(){
        int size = 0;
        long now = System.currentTimeMillis();
        if ((size = this.cache.size()- size)>0) {
            int i = lruQueue.size()-1;
            while (size > 0) {
                String key = lruQueue.get(i);
                Cache cache = this.cache.get(key);
                if (cache.timeout < now) {
                    this.cache.remove(key);
                }
                i--;
                size--;
            }
        }
    }


    class Cache{
        Object data;
        long timeout;

        public Cache(Object data, long timeout) {
            this.data = data;
            this.timeout = timeout;
        }
    }

}
