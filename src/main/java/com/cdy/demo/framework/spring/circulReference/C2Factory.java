package com.cdy.demo.framework.spring.circulReference;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("c2")
public class C2Factory implements FactoryBean<C2> {

    @Autowired
    C1 c1;

    @Autowired
    C1Factory c1Factor; //循环依赖注入时, 其实 C1Factory 还没有触发后置处理器, 也就是上面的c1中的c2其实还是空的


    public C2Factory() {
        System.out.println();
    }

    @Override
    public C2 getObject() throws Exception {
        C2 c2 = new C2();
        c2.c1 = c1;
        c2.c2 = c2;
        return c2;
    }

    @Override
    public Class<?> getObjectType() {
        return C2.class;
    }
}
