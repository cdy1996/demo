package com.cdy.demo.framework.spring.circulReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;


@Component("c2")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class C2 {
    
    @Autowired
    C1 c1;
    
    @Autowired
    C2 c2;
    
    public C1 getC1() {
        return c1;
    }
    
    public C2 getC2() {
        return c2;
    }
}
