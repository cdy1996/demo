package com.cdy.demo.java.generic;


/**
 * https://www.cnblogs.com/dengchengchao/p/9717097.html
 * 自限定类型一般用在继承体系中，需要参数协变的时候
 * 自限定泛型化, 把参数也泛型了
 * @param <E>
 */
public abstract class SelfLimiting<E extends SelfLimiting<E>> implements Comparable<E> {

    @Override
    public int compareTo(E o) {
        return 0;
    }
}
class Self1 extends SelfLimiting<Self1> {
}


abstract class Limiting implements Comparable<Limiting> {

    @Override
    public int compareTo(Limiting o) {
        return 0;
    }
}
class Self2 extends Limiting {
}