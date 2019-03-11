package com.cdy.demo.java;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * todo 描述
 * Created by 陈东一
 * 2018/10/1 16:45
 */
public class MyStream {
    
    /**
     * 1. 流的惰性求值
     * 2. 操作串联，一个元素经过所有的操作
     * 3. 短路操作
     */
    
    public static void main(String[] args) {
        List<String> list = Arrays.asList("aaa", "bbb", "ccc");
    
        for (String s : list) {
            s = s + "123";
            if (s.equals("ccc123")) {
                System.out.println(s);
            }
        }
        
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
        
        list.spliterator().forEachRemaining(e->{
            e = map.apply(e);
            if(filter.test(e)){
                forEach.accept(e);
            }
        });
        
        list.spliterator().forEachRemaining(e->{
            e = map.apply(e);
            if(filter.test(e)){
                forEach.accept(e);
            }
        });
    
   
    
    
        Set<String> ccc123 = list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).sorted().collect(Collectors.toCollection(LinkedHashSet::new));
//        Optional<String> ccc1231 = list.stream().map(e -> e + "123").filter(e -> e.equals("ccc123")).reduce((left, right) -> left + right);
        System.out.println(ccc123);
        
        Supplier<Set<String>> supplier = LinkedHashSet::new;
        Set<String> strings = supplier.get();
        BiConsumer<Set<String>,String> accumulator = (set, string) -> set.add(string);
        Consumer<String> consumer44 = c->accumulator.accept(strings, c);
        
        
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

