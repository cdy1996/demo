package com.cdy.demo.java;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class FunctionReference {


    public static void main(String[] args) {
        Consumer<String> consumer = (a) -> System.out.println(a);
        DoubleConsumer biConsumer = (a) -> System.out.println(a);

        test(consumer,"123");
        test(biConsumer::accept,123d);
        test((a) -> System.out.println(a),"123");
        test(System.out::println,"123");
    }

    public static <T> void test(Consumer<T> consumer, T a) {
        consumer.accept(a);
    }
}
