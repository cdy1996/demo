package com.hexin.demo.thread;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MethodHandleTest {



    public String toString(String s) {
        return "hello,"+s+"methodhandle";
    }

    public static void main(String[] args) {
        MethodHandleTest methodHandleTest = new MethodHandleTest();

        MethodHandle mh =null;
        try {
            mh = MethodHandles.lookup()
                    .findVirtual(MethodHandleTest.class,
                            "toString",
                            MethodType.methodType(String.class, String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        String invokeExact = null;
        try {
            invokeExact = (String) mh.invokeExact(methodHandleTest, "ssssss");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.out.println(invokeExact);


        MethodHandle methodHandle = mh.bindTo(methodHandleTest);
        String ssSSSSSS = null;
        try {
             ssSSSSSS = (String)methodHandle.invokeWithArguments("SsSSSSSS");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println(ssSSSSSS);


    }
}
