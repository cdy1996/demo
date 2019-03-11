package com.cdy.demo.framework.spring;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Inherited
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MyComponment {
}
