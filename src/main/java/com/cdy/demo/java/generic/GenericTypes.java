package com.cdy.demo.java.generic;

import java.util.List;

public class GenericTypes {



    public static <T> T method(List<T> list) {
        System.out.println("invoke method (List<Integer>list) ");

        Class<?> genericTypesClass = list.get(0).getClass();
        Class<?> genericTypesClas2s = list.getClass();
        System.out.println(genericTypesClass);
        System.out.println(genericTypesClas2s);

        return (T)list.get(0);
    }

    public static void main(String[] args) {

        int[] ints = {1, 2, 3};
//        List<Integer> list= [1,2,3];
//        List<String> strings = List.of("123");
//        GenericTypes.method(strings);

    }
}
