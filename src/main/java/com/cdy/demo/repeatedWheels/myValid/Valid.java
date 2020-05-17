package com.cdy.demo.repeatedWheels.myValid;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 校验参数框架 1
 * Created by 陈东一
 * 2020/5/16 0016 13:37
 */
public class Valid<T> {
    
    List<Supplier<Result>> list;
    
    public static <T> Valid<T> build() {
        return new Valid<T>();
    }
    
    public Valid<T> notNull(Supplier<T> s) {
        list.add(() -> {
            T t = s.get();
            boolean b = t != null;
            return new Result(b, t + "不能为null");
        });
        return this;
    }
    
    public Valid<T> lessThan(Supplier<?> a, int length) {
        list.add(() -> {
            Object o = a.get();
            boolean b = true;
            if (o instanceof CharSequence) {
                b = ((CharSequence) o).length() < length;
            } else if (o instanceof Collection) {
                b = ((Collection) o).size() < length;
            }
            return new Result(b, o + "长度要小于" + length);
        });
        return this;
    }
    
    public Optional<Result> valid() {
        return list.stream().map(Supplier::get).filter(e -> !e.surrcess).findFirst();
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
