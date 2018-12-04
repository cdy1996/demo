package com.hexin.demo.java8;


import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamIn9 {

    public static void main(String[] args) {
        StreamIn9 streamIn9 = new StreamIn9();
        streamIn9.test();
    }

    public void test(){


        List<String> of = List.of("1,11", "2,22", "3,33");

        List<String> collect = of.stream().collect(Collectors.filtering(e -> !e.isEmpty(), Collectors.toUnmodifiableList()));
        List<String> collect1 = of.stream().collect(Collectors.flatMapping(e -> Arrays.stream(e.split(",")), Collectors.toUnmodifiableList()));



        Predicate<Integer> predicate = e -> e > 10;
        Tes1t<Integer> t = e -> e > 10;
        Boolean abc = abc(t::test, 15);
        Predicate<Integer> and = predicate.or(e -> e < 20).and(e -> e == 15);

    }



    public Boolean abc(Predicate<Integer> predicate, Integer a) {
        return predicate.test(a);
    }
}
interface Tes1t<T>{
    boolean test(T t);
}