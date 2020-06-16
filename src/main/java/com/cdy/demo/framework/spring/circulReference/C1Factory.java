package com.cdy.demo.framework.spring.circulReference;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("c1")
public class C1Factory implements FactoryBean<C1> {

    @Autowired
    C2 c2;

    @Autowired
    C2Factory c2Factory;

    public C1Factory() {
        System.out.println();
    }

    @Override
    public C1 getObject() throws Exception {
        C1 c1 = new C1();
        c1.c2 = c2;
        return c1;
    }

    @Override
    public Class<?> getObjectType() {
        return C1.class;
    }
}
