package com.cdy.demo.structure;

import java.util.Iterator;
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

    List<String> writeQueue = new LinkedList<>();
    List<String> readQueue = new LinkedList<>();

//    Set queue = new TreeSet();

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
                writeQueue.add(key);
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
                    readQueue.remove(key);
                    result = null;
                } else{
                    result = o.data;
                    readQueue.add(key);
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

    public void release(){
        long now = System.currentTimeMillis();
        Iterator<String> iterator = writeQueue.iterator();
        while(!iterator.hasNext()) {
            String key = iterator.next();
            Cache cache = this.cache.get(key);
            if (cache.timeout < now) {
                this.cache.remove(key);
            }
            iterator.remove();
        }
       iterator = readQueue.iterator();
        while(!iterator.hasNext()) {
            String key = iterator.next();
            Cache cache = this.cache.get(key);
            if (cache.timeout < now) {
                this.cache.remove(key);
            }
            iterator.remove();
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
