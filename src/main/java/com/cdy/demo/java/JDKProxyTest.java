package com.cdy.demo.java;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * todo 描述
 * Created by 陈东一
 * 2018/3/6 14:42
 */

public class JDKProxyTest {
    
    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Foo1 foo = new Foo2();
        InvocationHandlerImpl handler = new InvocationHandlerImpl(foo);
//        Foo1 o = (Foo1)Proxy.newProxyInstance(foo.getClass().getClassLoader(), Foo1.class.getInterfaces(), handler);
//        o.get();

        Foo1 f = (Foo1) handler.newProxy(foo);
        System.out.println(f.getClass());
        f.get();
//        f.before();
//        f.before();

    }
}

interface Foo1 {
    
    void get();
    
    void before();
}

class Foo2 implements Foo1 {
    
    @Override
    public void get() {
        System.out.println("外部方法");
        before();
    }
    
    @Override
    public void before() {
        System.out.println("内部方法");
    }
}

class InvocationHandlerImpl implements InvocationHandler {
    Object foo;
    
    InvocationHandlerImpl(Object foo) {
        this.foo = foo;
    }
    
    public Object newProxy(Object targetObject) {//将目标对象传入进行代理
        this.foo = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), this);//返回代理对象
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("jdk动态代理前 ");
        return method.invoke(foo, args);
    }
}
