package com.hexin.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * lambda导致的死锁
 * Created by viruser on 2018/7/9.
 */
public class LambdaTest {


    String a;

    public LambdaTest(String a) {
        this.a = a;
    }

    public LambdaTest() {
    }

    static{
       // getName();
    }


    public static void main(String[] args) {

        Map<String, Function<String, LambdaTest>> map = new HashMap<>();
        map.put("apple", LambdaTest::new);
        map.put("123", LambdaTest::new);

        LambdaTest apply = map.get("apple").apply("aaa");
        Predicate<String> apple = a -> a.toLowerCase().startsWith("1");
        boolean test = apple.and(a -> a.toLowerCase().endsWith("1")).test("123");
        System.out.println(test);

    }


    public static String getName() {
        Callable<String> call = LambdaTest::getSex;
        new Thread(()->{
            try {
                String call1 = call.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        FutureTask<String> futureTask = new FutureTask<>(call);
        new Thread(futureTask).start();
        String s1 = null;
        try {
            s1 = futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return s1;

    }

    public static String getSex(){
        return "123";
    }
}
