package com.cdy.demo.java.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * https://segmentfault.com/a/1190000019555458
 * Created by 陈东一
 * 2020/3/7 0007 18:52
 */
@Slf4j
public class CountedCompleterDemo {
    
    @Test
    public void testDivideSearch(){
        Integer[] array = new Integer[10000000];
        for(int i = 0; i < array.length; i++){
            array[i] = i+1;
        }
        AtomicReference<Integer> result = new AtomicReference<>();
        Integer find = new Searcher<>(null, array, result, 0,
                array.length - 1,this::match).invoke();
        log.info("查找结束,任务返回:{},result:{}",find,result.get());
        
    }
    
    static class Searcher<E> extends CountedCompleter<E> {
        
        final E[] array; final AtomicReference<E> result; final int lo, hi;
        final Function<E,Boolean> matcher;
        
        Searcher(CountedCompleter<?> p, E[] array, AtomicReference<E> result,
                 int lo, int hi,Function<E,Boolean> matcher){
            super(p);
            this.array = array;
            this.result = result;
            this.lo = lo;
            this.hi = hi;
            this.matcher = matcher;
        }
        @Override
        public void compute() {
            int l = this.lo;int h = this.hi;
            while(result.get() == null && h >= l){
                
                if(h - l >=2){
                    int mid = (l + h)>>>1;
                    //添加挂起任务数量,这样当出现tryComplete时可以触发root的结束(未查到)
                    addToPendingCount(1);
                    new Searcher<E>(this,array,result,mid,h,matcher).fork();
                    h = mid;
                }else{
                    E x = array[l];
                    if(matcher.apply(x) &&  result.compareAndSet(null,x)){
                        super.quietlyCompleteRoot();
                    }
                    break;
                }
            }
            //当前未有任何一个线程查到结果,当前任务也完成了子集查找,减少一个挂起数量,若挂起数已减至0则终止.
            if(null == result.get())
                tryComplete();
        }
        
    }
    
    private boolean match(Integer x) {
        return x > 2000000 &&  x%2 ==0 && x%3 == 0 && x%5 ==0 && x %7 ==0;
    }
    
    
    @Test
    public void testMapReduce() {
        Integer[] array = {1, 2, 3};
        //方法一.
        Integer result = new MapRed<>(null, array, (a) -> a + 2, (a, b) -> a + b, 0, array.length).invoke();
        log.info("方法一result:{}",result);
        //方法二我就不抄了,就在官方注释上.
//        result = new MapReducer<>(null, array, (a) -> a + 1
//                , (a, b) -> a + b, 0, array.length, null).invoke();
        log.info("方法二result:{}", result);
        
    }
    
    
    /**
     * 第一种map reduce方式,很好理解.
     * @param <E>
     */
    private static class MapRed<E> extends CountedCompleter<E> {
        final E[] array;
        final MyMapper<E> mapper;
        final MyReducer<E> reducer;
        final int lo, hi;
        MapRed<E> sibling;//兄弟节点的引用
        E result;
        
        MapRed(CountedCompleter<?> p, E[] array, MyMapper<E> mapper,
               MyReducer<E> reducer, int lo, int hi) {
            super(p);
            this.array = array;
            this.mapper = mapper;
            this.reducer = reducer;
            this.lo = lo;
            this.hi = hi;
        }
        
        public void compute() {
            if (hi - lo >= 2) {
                int mid = (lo + hi) >>> 1;
                MapRed<E> left = new MapRed(this, array, mapper, reducer, lo, mid);
                MapRed<E> right = new MapRed(this, array, mapper, reducer, mid, hi);
                left.sibling = right;
                right.sibling = left;
                //只挂起右任务
                setPendingCount(1);
                right.fork();
                //直接运算左任务.
                left.compute();
            } else {
                if (hi > lo)
                    result = mapper.apply(array[lo]);
                //它会依次调用onCompletion.并且是自己调自己或completer调子,
                //且只有左右两个子后完成的能调成功(父任务的挂起数达到0).
                tryComplete();
            }
        }
        
        public void onCompletion(CountedCompleter<?> caller) {
            //忽略自己调自己.
            if (caller != this) {
                //参数是子任务.
                MapRed<E> child = (MapRed<E>) caller;
                MapRed<E> sib = child.sibling;
                //设置父的result.
                if (sib == null || sib.result == null)
                    result = child.result;
                else
                    result = reducer.apply(child.result, sib.result);
            }
        }
        
        public E getRawResult() {
            return result;
        }
    }
    //mapper和reducer简单的不能再简单.
    @FunctionalInterface
    private static interface MyMapper<E> {
        E apply(E e);
    }
    @FunctionalInterface
    private static interface MyReducer<E> {
        E apply(E a, E b);
    }
}
