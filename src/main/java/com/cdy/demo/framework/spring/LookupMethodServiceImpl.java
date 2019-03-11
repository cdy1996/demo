package com.cdy.demo.framework.spring;


import org.springframework.beans.factory.annotation.Lookup;

//@Service
public abstract class LookupMethodServiceImpl implements LookupMethodService {

    @Override
    public User getUser() {
        return creatUser();
    }

    @Lookup("user")
    public abstract User creatUser();

}
