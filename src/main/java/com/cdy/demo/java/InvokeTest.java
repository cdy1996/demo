package com.cdy.demo.java;

import org.junit.Test;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * https://www.iteye.com/topic/1129340
 * Created by 陈东一
 * 2019/8/17 0017 16:42
 */
public class InvokeTest {
    public static interface Getter {
        Object get(Object obj);
    }
    
    public static interface Setter {
        void set(Object obj, Object value);
    }
    
    @Test
    public void testInvoke() {
        try {
            
            Date date = new Date();
            
            Method getTime = Date.class.getMethod("getTime");
            Method setTime = Date.class.getMethod("setTime", Long.TYPE);
            getTime.setAccessible(true);
            setTime.setAccessible(true);
            FastClass fastClass = FastClass.create(Date.class);
            FastMethod fastGetTime = fastClass.getMethod(getTime);
            FastMethod fastSetTime = fastClass.getMethod(setTime);
            
            long t = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                date.setTime(33333333L);
                date.getTime();
            }
            long t1 = System.currentTimeMillis();
            System.out.println("直接调用耗时：" + (t1 - t) + "ms");
            long t2 = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                setTime.invoke(date, 6666666L);
                getTime.invoke(date);
            }
            long t3 = System.currentTimeMillis();
            System.out.println("JDK反射调用耗时：" + (t3 - t2) + "ms");
            t3 = System.currentTimeMillis();
            for (int i = 0; i < 100000000; i++) {
                fastSetTime.invoke(date, new Object[]{6666666L});
                fastGetTime.invoke(date, new Object[]{});
            }
            long t4 = System.currentTimeMillis();
            System.out.println("FastMethod调用耗时：" + (t4 - t3) + "ms");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
