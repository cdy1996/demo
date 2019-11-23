package com.cdy.demo.java;

import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

import static com.cdy.demo.java.Teeing.teeing;

/**
 * stream的分解操作
 * 1. 流的惰性求值
 * 2. 操作串联，一个元素经过所有的操作
 * 3. 短路操作
 * Created by 陈东一
 * 2018/10/1 16:45
 */
public class MyStream {
    
    List<String> list = Arrays.asList("aaa", "bbb", "ccc");
    
    static class Combine{
        Set<String> a;
        Set<String> b;
    
        public Combine(Set<String> a, Set<String> b) {
            this.a = a;
            this.b = b;
        }
    
        @Override
        public String toString() {
            return "Combine{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }
    
    public static void main(String[] args) {
        Combine ccc123 = new  MyStream().list.stream().collect(teeing(Collectors.toSet(), Collectors.toSet(), Combine::new));
        System.out.println(ccc123);
    }
    
    @Test
    public void t(){
        Combine ccc123 = list.stream().collect(teeing(Collectors.toSet(), Collectors.toSet(), Combine::new));
        System.out.println(ccc123);
    }
    
    @Test
    public void test0() {
        
        list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).forEach(System.out::println);
        
        
        Spliterator<String> spliterator = list.spliterator();
        
        Function<String, String> map = e -> e + "123";
        Predicate<String> filter = e -> e.equals("ccc123");
        Consumer<String> forEach = e -> System.out.println(e);
      
        
        spliterator.forEachRemaining(e ->{
            String apply = map.apply(e);
            if (filter.test(apply)) {
                forEach.accept(apply);
            }
        });
        
  
        
        
    }
    
    /**
     * for (String s : list) {
     *    s = s + "123";
     *    if (s.equals("ccc123")) {
     *          System.out.println(s);
     *    }
     * }
     */
    @Test
    public void test() {
        
        list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).forEach(System.out::println);
        
        
        Spliterator<String> spliterator = list.spliterator();
        
        Function<String, String> map = e -> e + "123";
        Predicate<String> filter = e -> e.equals("ccc123");
        Consumer<String> forEach = e -> System.out.println(e);
        
        Consumer<String> consumer3 = c -> forEach.accept(c);
        Consumer<String> consumer2 = c -> {
            if (filter.test(c))
                consumer3.accept(c);
            
        };
        Consumer<String> consumer1 = c -> consumer2.accept(map.apply(c));
        
        spliterator.forEachRemaining(e -> consumer1.accept(e));
   
        
    }
    
    @Test
    public void test2() {
    
        Function<String, String> map = e -> e + "123";
        Predicate<String> filter = e -> e.equals("ccc123");
        Consumer<String> forEach = e -> System.out.println(e);
    
    
        Set<String> ccc123 = list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
//        Optional<String> ccc1231 = list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).reduce((left, right) -> left + right);
        System.out.println(ccc123);
        
        Supplier<Set<String>> supplier = LinkedHashSet::new;
        Set<String> strings = supplier.get();
        BiConsumer<Set<String>, String> accumulator = (set, string) -> set.add(string);
        Consumer<String> consumer44 = c -> accumulator.accept(strings, c);
        
        
        List<String> sort = new ArrayList<>();
        Consumer<String> consumer33 = c -> sort.add(c);
        Consumer<String> consumer22 = c -> {
            if (filter.test(c))
                consumer33.accept(c);
            
        };

//        Consumer<String> consumer11 = c -> consumer22.accept(map.apply(c));
        Consumer<String> consumer11 = c -> consumer22.accept(map.apply(c));
        list.spliterator().forEachRemaining(e -> consumer11.accept(e));
        Collections.sort(sort);
        sort.forEach(consumer44);
        
        System.out.println(strings);
    }
    
    
    
    
}



