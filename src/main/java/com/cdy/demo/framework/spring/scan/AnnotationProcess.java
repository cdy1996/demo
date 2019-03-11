package com.cdy.demo.framework.spring.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationProcess {

    boolean process(TestContext context, Annotation annotation, Method method, Object[] args, int paramIndex);

}
