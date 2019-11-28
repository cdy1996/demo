package com.cdy.demo.java;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * todo
 * Created by 陈东一
 * 2019/11/23 0023 19:43
 */
public class Teeing {
    
    public static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_UNORDERED_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();
    public static final Set<Collector.Characteristics> CH_CONCURRENT_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED,
            Collector.Characteristics.IDENTITY_FINISH));
    public static final Set<Collector.Characteristics> CH_CONCURRENT_NOID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.CONCURRENT,
            Collector.Characteristics.UNORDERED));
    
    public static <T, R1, R2, R>
    Collector<T, ?, R> teeing(Collector<? super T, ?, R1> downstream1,
                              Collector<? super T, ?, R2> downstream2,
                              BiFunction<? super R1, ? super R2, R> merger) {
        return teeing0(downstream1, downstream2, merger);
    }
    
    private static <T, A1, A2, R1, R2, R>
    Collector<T, ?, R> teeing0(Collector<? super T, A1, R1> downstream1,
                               Collector<? super T, A2, R2> downstream2,
                               BiFunction<? super R1, ? super R2, R> merger) {
        Objects.requireNonNull(downstream1, "downstream1");
        Objects.requireNonNull(downstream2, "downstream2");
        Objects.requireNonNull(merger, "merger");
        
        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), "downstream1 supplier");
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), "downstream2 supplier");
        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), "downstream1 accumulator");
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), "downstream2 accumulator");
        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), "downstream1 combiner");
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), "downstream2 combiner");
        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), "downstream1 finisher");
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), "downstream2 finisher");
        
        Set<Collector.Characteristics> characteristics;
        Set<Collector.Characteristics> c1Characteristics = downstream1.characteristics();
        Set<Collector.Characteristics> c2Characteristics = downstream2.characteristics();
        if (CH_ID.containsAll(c1Characteristics) || CH_ID.containsAll(c2Characteristics)) {
            characteristics = CH_NOID;
        } else {
            EnumSet<Collector.Characteristics> c = EnumSet.noneOf(Collector.Characteristics.class);
            c.addAll(c1Characteristics);
            c.retainAll(c2Characteristics);
            c.remove(Collector.Characteristics.IDENTITY_FINISH);
            characteristics = Collections.unmodifiableSet(c);
        }
    
    
        class PairBox {
            A1 left = c1Supplier.get();
            A2 right = c2Supplier.get();
        
            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(right, t);
            }
        
            PairBox combine(PairBox other) {
                left = c1Combiner.apply(left, other.left);
                right = c2Combiner.apply(right, other.right);
                return this;
            }
        
            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(right);
                return merger.apply(r1, r2);
            }
        }
//        return new CollectorImpl<>(PairBox::new, PairBox::add, PairBox::combine, PairBox::get, characteristics);
        return new Collector<T, PairBox, R>() {
            @Override
            public Supplier<PairBox> supplier() {
                return PairBox::new;
            }
        
            @Override
            public BiConsumer<PairBox, T> accumulator() {
                return PairBox::add;
            }
        
            @Override
            public BinaryOperator<PairBox> combiner() {
                return PairBox::combine;
            }
        
            @Override
            public Function<PairBox, R> finisher() {
                return PairBox::get;
            }
        
            @Override
            public Set<Characteristics> characteristics() {
                return characteristics;
            }
        };
    
    }
    
    
    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;
        
        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A,R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }
        
        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }
        
        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }
        
        @Override
        public Supplier<A> supplier() {
            return supplier;
        }
        
        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }
        
        @Override
        public Function<A, R> finisher() {
            return finisher;
        }
        
        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }
}
