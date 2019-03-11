package com.cdy.demo.structure.myhashmap;

/**
 * Created by 陈东一
 * 2018/2/27 14:38
 */
public interface MyMap<K, V> {
    
    public V put(K k, V v);
    
    public V get(K k);
    
    public interface Entry<K, V> {
        public K getKey();

        public V getValue();

    }
}
