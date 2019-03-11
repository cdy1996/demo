package com.cdy.demo.java.thinkInJava;

import java.util.AbstractMap;
import java.util.Set;

/**
 * Created by 陈东一 on 2017/9/20 15:33
 */
public class SlowMap<K, V> extends AbstractMap<K, V> {
    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return super.put(key, value);
    }
}
