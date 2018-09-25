package com.hexin.demo.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MyComponment {
}
