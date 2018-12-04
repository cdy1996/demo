package com.hexin.demo.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface AnnotationProcess {

    boolean process(TestContext context, Annotation annotation, Method method, Object[] args, int paramIndex);

}
