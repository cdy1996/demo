package com.cdy.demo.java.tailRecursion;


import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


/**
 * 另一个尾递归
 * @param <T>
 * @param <P>
 */
public class Tail<T, P> {
    private T init;
    private Predicate<T> when;
    private UnaryOperator<T> loop;
    private Function<T, P> done;
 
    public Tail(T init) {
        this.init = init;
    }
 
    public static <T, P> Tail<T, P> init(T init) {
        return new Tail<>(init);
    }
 
    public Tail<T, P> loop(UnaryOperator<T> loop) {
        this.loop = loop;
        return this;
    }
 
    public Tail<T, P> when(Predicate<T> when) {
        this.when = when;
        return this;
    }
 
    public Tail<T, P> done(Function<T, P> done) {
        this.done = done;
        return this;
    }
 
    public Optional<P> exec() {
        return Stream.iterate(init, loop)
                .filter(when)
                .findFirst()
                .map(done);
    }
 
    public static void main(String[] args) {
        System.out.println(Tail.<Pair, Integer>init(new Pair(1, 100_000))
                .loop(p -> new Pair(p.getValue0() + p.getValue1(), p.getValue1() - 1))
                .when(p -> p.getValue1() == 1)
                .done(p -> p.getValue0())
                .exec()
                .orElse(0));
    }
 
    private static class Pair {
        private final int p0;
        private final int p1;
 
        public Pair(int p0, int p1) {
            this.p0 = p0;
            this.p1 = p1;
        }
 
        public int getValue0() {
            return p0;
        }
 
        public int getValue1() {
            return p1;
        }
    }
}