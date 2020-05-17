package com.cdy.demo.repeatedWheels.myValid;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

/**
 * 校验参数框架2  参考stream的设计, 好处是可以复用, 因为不是公用同一个list
 * Created by 陈东一
 * 2020/5/16 0016 13:37
 */
public class Valid2<T> {
    
    final Valid2<T> pre;
    final Supplier<Result> s;
    
    
    public Valid2(Valid2<T> tValid2, Supplier<Result> o) {
        pre = tValid2;
        s = o;
    }
    
    public static <T> Valid2<T> build() {
        return new Valid2<T>(null, null);
    }
    
    public Valid2<T> notNull(Supplier<T> s) {
        return new Valid2<T>(this, () -> {
            T t = s.get();
            boolean b = t != null;
            return new Result(b, t + "不能为null");
        });
    }
    
    public Valid2<T> lessThan(Supplier<?> a, int length) {
        return new Valid2<T>(this, () -> {
            Object o = a.get();
            boolean b = true;
            if (o instanceof CharSequence) {
                b = ((CharSequence) o).length() < length;
            } else if (o instanceof Collection) {
                b = ((Collection) o).size() < length;
            }
            return new Result(b, o + "长度要小于" + length);
        });
    }
    
    public Optional<Result> valid() {
        LinkedBlockingDeque<Valid2<T>> deque = new LinkedBlockingDeque<>();
        Valid2<T> v = this;
        while (v != null) {
            deque.addFirst(this);
            v = v.pre;
        }
        return deque.stream().map(e -> e.s).map(Supplier::get).filter(e -> !e.surrcess).findFirst();
        
    }
    
    static class Result {
        boolean surrcess;
        String resuilt;
        
        public Result(boolean surrcess, String resuilt) {
            this.surrcess = surrcess;
            this.resuilt = resuilt;
        }
    }
    
}
