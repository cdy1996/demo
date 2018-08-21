package com.hexin.demo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by viruser on 2018/7/9.
 */
public class ExeceptionTest {


    public static void main(String[] args) {

        FutureTask<Integer> integerFutureTask = get3();
        try {
            integerFutureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static FutureTask<Integer> get3(){
        FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> {
            try {
                throw new Exception("!23");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(integerFutureTask).start();
        return integerFutureTask;
    }

    public static void get5(){
        new Thread(()-> {
            try {
                throw new Exception("!23");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        ).start();
    }

    public static void get4(){
            try {
                throw new Exception("!23");
            } catch (Exception e) {

                throw new RuntimeException(e);
            }
    }

    public static void get2() throws Throwable {
        try {
            get();
        } catch (Exception e) {
            throw new RuntimeException().initCause(e);
        }
    }


    public static String get() throws Exception {
        throw new Exception("123");
    }
}
