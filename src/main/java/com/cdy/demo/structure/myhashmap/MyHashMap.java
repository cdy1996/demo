package com.cdy.demo.structure.myhashmap;

import java.util.ArrayList;
import java.util.List;

/**
 * 手写hashmap
 * Created by 陈东一
 * 2018/2/27 14:37
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    //数组大小
    public static int defaultLength = 1 << 4;
    //扩容标准  过大扩容概率变低，存储小，存取效率低，会形成链表，要大量遍历
    public static double defaultAddSizeFactor = 0.75;
    
    public int useSize;
    public Entry<K, V>[] table = null;
    
    public MyHashMap() {
        this(defaultLength, defaultAddSizeFactor);
    }

    public MyHashMap(int length, double addSizeFactor) {
        if (length < 0) {
            throw new IllegalArgumentException("必须大于0");
        }
        this.defaultLength = length;
        this.defaultAddSizeFactor = addSizeFactor;
        table = new Entry[defaultLength];
    }
    
    class Entry<K, V> implements MyMap.Entry<K, V> {
        K k;
        V v;
        Entry<K, V> next; //指向this挤压下去的entry对象

        public Entry(K k, V v, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }
    }
    
    
    @Override
    public V put(K k, V v) {
        if (useSize > defaultAddSizeFactor * defaultLength) {
            up2Size();
        }
        int index = getIndex(k, table.length);
        
        Entry<K, V> entry = table[index];
        if (entry == null) {
            table[index] = new Entry<>(k, v, null);
            useSize++;
        } else {
            table[index] = new Entry<K, V>(k, v, entry);
        }
        return table[index].getValue();
        
    }
    
    private int getIndex(K k, int length) {
        int m = length - 1;
        //index -> [0,length)
//        System.out.print("key:"+k);
        int index = hash(k.hashCode()) & m;
//        System.out.print(" index:"+index);
//        System.out.println();
        return index;
    }
    
    private int hash(int hashCode) {
        hashCode = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return hashCode ^ ((hashCode >>> 7) ^ (hashCode >>> 4));
    }
    
    private void up2Size() {
        Entry<K, V>[] newTable = new Entry[2 * defaultLength];
        againHash(newTable);
    }
    
    
    private void againHash(MyHashMap<K, V>.Entry<K, V>[] newTable) {
        List<Entry<K, V>> entryList = new ArrayList<Entry<K, V>>();
        //找就数组中的对象
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) {
                continue;
            }
            foundEntryByNext(table[i], entryList);
        }
        if (entryList.size() > 0) {
            System.out.println("=============resize==================");
            useSize = 0;
            defaultLength = 2 * defaultLength;
            table = newTable;
            for (Entry<K, V> entry : entryList) {
                //取消链表操作
                if (entry.next != null) {
                    entry.next = null;
                }
                put(entry.getKey(), entry.getValue());
            }
            System.out.println("=============resize  end==================");
        }
        
    }
    
    //找到不为空对象存入list
    private void foundEntryByNext(Entry<K, V> entry, List<Entry<K, V>> entryList) {
        if (entry != null && entry.next != null) {
            entryList.add(entry);
            //递归
            foundEntryByNext(entry.next, entryList);
        } else {
            entryList.add(entry);
        }
    }
    
    @Override
    public V get(K k) {
        int index = getIndex(k, table.length);
        if (table[index] == null) {
            throw new NullPointerException();
        }
        return findValueByEqualKey(k, table[index]);
    }
    
    private V findValueByEqualKey(K k, Entry<K, V> entry) {
        if (k == entry.getKey() || k.equals(entry.getKey())) {
            return entry.getValue();
        } else if (entry.next != null) {
            return findValueByEqualKey(k, entry.next);
        }
        return null;

    }
}
