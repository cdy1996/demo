package com.cdy.demo.framework;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

public class JsonTest {

    @Data
    static
    class  A{
        String a;
    }
    @Data
    static
    class B{
        A a;
        String b;
    }
    @Data
    static
    class C<T>{
        T a;
        String b;
    }
    @Data
    static
    class D{
        Object a;
        String b;
    }

    @Data
    static
    class E<T>{
        List<T> a;
        String b;
    }


    public static void main(String[] args) {
        A a = new A();
        a.a="a";

        B b = new B();
        b.a= a;
        b.b = "b";
        System.out.println(JSON.parseObject(JSON.toJSONString(b), B.class).a.getClass());

        C<A> c = new C<>();
        c.a= a;
        c.b = "b";
        System.out.println(JSON.parseObject(JSON.toJSONString(c), C.class).a.getClass());

        D d = new D();
        d.a= a;
        d.b = "b";
        System.out.println(JSON.parseObject(JSON.toJSONString(d), D.class).a.getClass());

    }
}
