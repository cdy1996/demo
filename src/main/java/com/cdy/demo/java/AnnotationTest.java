package com.cdy.demo.java;

import org.junit.Test;

import java.lang.annotation.*;

public class AnnotationTest {

    @Test
   public void tets() throws NoSuchMethodException {
        Class<ccc> cccClass = ccc.class;
        c annotation = cccClass.getAnnotation(c.class);
        b annotation1 = annotation.annotationType().getAnnotation(b.class);
        System.out.println(annotation1);

    }

}
@c
class abc{

    @c
    public void test(){

    }

}

class ccc extends abc{


}


@Inherited
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface a{
    String value() default "";
}

@Inherited
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@a("b")
@interface b{
    String value() default "";
}

@Inherited
@Target(value = {ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@b("c")
@interface c{

}