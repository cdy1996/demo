package com.hexin.demo.spring;


import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

//@Service
public abstract class LookupMethodServiceImpl implements LookupMethodService {

    @Override
    public User getUser() {
        return creatUser();
    }

    @Lookup("user")
    public abstract User creatUser();

}
