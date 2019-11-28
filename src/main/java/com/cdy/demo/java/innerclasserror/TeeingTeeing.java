package com.cdy.demo.java.innerclasserror;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * todo
 * 2019/11/23 0023 19:43
 */
public class TeeingTeeing {
    
    
    public static void main(String[] args) {


//        return new CollectorImpl<>(PairBox::new, PairBox::add, PairBox::combine, PairBox::get, characteristics);
    
        Set<String> collect = Arrays.asList("1", "2", "3").stream().collect(get(Collectors.toSet()));
        System.out.println(collect);
    }
    
    
    static <T, A, R> Collector<T, ?, R> get(Collector<T, A, R> collector) {
        
        final Supplier<A> supplier = collector.supplier();
        final BiConsumer<A, T> accumulator = collector.accumulator();
        final BinaryOperator<A> combiner = collector.combiner();
        final Function<A, R> finisher = collector.finisher();
  
        class PairBox {
            private A a = supplier.get();
            
            private void add(T t) {
                accumulator.accept(a, t);
            }
            
            private PairBox combine(PairBox other) {
                combiner.apply(a, other.a);
                return this;
            }
            
            private R get() {
                return finisher.apply(a);
            }
        }
        
        
        
        // jdk实现的方式, 通过暂存函数
      /*  class C implements Collector<T, PairBox, R> {
           final Supplier<PairBox> supplier1;
           final BiConsumer<PairBox, T> accumulator1;
           final BinaryOperator<PairBox> combiner1;
           final Function<PairBox, R> finisher1;
            
            public C(Supplier<PairBox> supplier1, BiConsumer<PairBox, T> accumulator1, BinaryOperator<PairBox> combiner1, Function<PairBox, R> finisher1) {
                this.supplier1 = supplier1;
                this.accumulator1 = accumulator1;
                this.combiner1 = combiner1;
                this.finisher1 = finisher1;
            }
            
            @Override
            public Supplier<PairBox> supplier() {
                return supplier1;
            }
            
            @Override
            public BiConsumer<PairBox, T> accumulator() {
                return accumulator1;
            }
            
            @Override
            public BinaryOperator<PairBox> combiner() {
                return combiner1;
            }
            
            @Override
            public Function<PairBox, R> finisher() {
                return finisher1;
            }
            
            @Override
            public Set<Characteristics> characteristics() {
                HashSet<Characteristics> characteristics = new HashSet<>();
                characteristics.add(Characteristics.UNORDERED);
                return characteristics;
            }
        }
        return new C(PairBox::new,
                PairBox::add,
                PairBox::combine,
                PairBox::get );*/
    
    
      // 正确C类的所有方法引用改为 匿名类
        class C implements Collector<T, PairBox, R> {
    
           
    
            @Override
            public Supplier<PairBox> supplier() {
                return new Supplier<PairBox>() {
                    @Override
                    public PairBox get() {
                        return new PairBox();
                    }
                };
            }
        
            @Override
            public BiConsumer<PairBox, T> accumulator() {
                return PairBox::add;
            }
        
            @Override
            public BinaryOperator<PairBox> combiner() {
                return new BinaryOperator<PairBox>() {
                    @Override
                    public PairBox apply(PairBox pairBox, PairBox pairBox2) {
                        return pairBox.combine(pairBox2);
                    }
                };
            }
        
            @Override
            public Function<PairBox, R> finisher() {
                return new Function<PairBox, R>() {
                    @Override
                    public R apply(PairBox pairBox) {
                        return pairBox.get();
                    }
                };
            }
        
            @Override
            public Set<Characteristics> characteristics() {
                HashSet<Characteristics> characteristics = new HashSet<>();
                characteristics.add(Characteristics.UNORDERED);
                return characteristics;
            }
        };
    
        
        // 错误 在编译后会有问题
       /* class C1 implements Collector<T, PairBox, R> {
        
        
        
            @Override
            public Supplier<PairBox> supplier() {
                return new Supplier<PairBox>() {
                    @Override
                    public PairBox get() {
                        return new PairBox();
                    }
                };
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
                HashSet<Characteristics> characteristics = new HashSet<>();
                characteristics.add(Characteristics.UNORDERED);
                return characteristics;
            }
        };*/
        return new C();
    }
    
    ;
    
}
