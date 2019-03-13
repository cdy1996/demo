package com.cdy.demo.framework.spring.scan;

public @interface Remoting {
    //serviceid
    String name() default "123";
    //context-path
    String context() default "123";

    String configuration() default "defaultConfiguration";

}
