package com.cdy.demo.java.classloadertest;

import java.lang.reflect.Method;

/**
 * https://blog.csdn.net/belalds/article/details/83105685
 */
public class Test {

    private Object fooTestInstance;
    private FooClassLoader fooClassLoader = new FooClassLoader();

    public static void main(String[] args) throws Exception {
        Test test = new Test();
        test.initAndLoad();
        Object fooTestInstance = test.getFooTestInstance();
        System.out.println(fooTestInstance.getClass().getClassLoader());

        Method getFoo = fooTestInstance.getClass().getMethod("getFoo");
        System.out.println(getFoo.invoke(fooTestInstance));

        System.out.println(test.getClass().getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());

    }


    public void initAndLoad() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> aClass = Class.forName("com.cdy.demo.java.classloadertest.FooTest", true, fooClassLoader);
        fooTestInstance = aClass.newInstance();
    }

    public Object getFooTestInstance() {
        return fooTestInstance;
    }
}
