package com.cdy.demo.framework.spring.scan;

public @interface Test {
    //serviceid
    String name() default "123";
    //context-path
    String context() default "123";

    String configuration() default "defaultConfiguration";

}
