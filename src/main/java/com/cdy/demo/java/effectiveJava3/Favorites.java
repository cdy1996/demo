package com.cdy.demo.java.effectiveJava3;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 第33条
 */
public class Favorites {

    Map<Class<?>, Object> map = new HashMap<>();

    public <T> void putFavorite(Class<T> type, T instance) {
//        map.put(type, instance); 自己的实现,下面是书本上的实现
        map.put(Objects.requireNonNull(type), type.cast(instance));
    }

    public <T> T getFavorite(Class<T> type) {
//        return  (T)(map.get(type)); 自己的实现,下面是书本上的实现
        return type.cast(map.get(type));
    }


    public static void main(String[] args) {
        Favorites f = new Favorites();
        //不能存放List<String>.class
        f.putFavorite(String.class, "Java");
        f.putFavorite(Integer.class, 0xcafebabe);
        f.putFavorite(Class.class, Favorites.class);
        String favoritesString = f.getFavorite(String.class);
        int favoriteinteger = f.getFavorite(Integer.class);
        Class<?> favoriteClass = f.getFavorite(Class.class);
        System.out.printf("%s %x %s%n ", favoritesString,
                favoriteinteger, favoriteClass.getName());
    }
}