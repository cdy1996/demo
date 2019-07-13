package com.cdy.demo.repeatedWheels.myaop.Test;

import com.cdy.demo.repeatedWheels.myaop.advice.AfterAdvice;
import com.cdy.demo.repeatedWheels.myaop.advice.AroundAdvice;
import com.cdy.demo.repeatedWheels.myaop.advice.BeforeAdvice;
import com.cdy.demo.repeatedWheels.myaop.advisor.DefaultAdvisor;
import com.cdy.demo.repeatedWheels.myaop.pointcut.ClassPointCut;
import com.cdy.demo.repeatedWheels.myaop.proxy.CglibProxyFactory;
import com.cdy.demo.repeatedWheels.myaop.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.function.Supplier;

/**
 * todo
 * Created by 陈东一
 * 2019/7/13 0013 13:45
 */
public class MainTest {
    
    public static void main(String[] args) {
        Sleep sleep = new SleepImpl();
    
        DefaultAdvisor advisor = new DefaultAdvisor(new ClassPointCut(Sleep.class)).addAdvice(new BeforeAdvice() {
            @Override
            public <T> void invokeBefore(Class<T> clazz, Method method, Object[] args) {
                System.out.println("before");
            }
        }).addAdvice(new AfterAdvice() {
            @Override
            public <T> Object invokeAfter(Class<T> clazz, Method method, Object[] args, Object result, Exception exception) {
                System.out.println("after");
                return result;
            }
        }).addAdvice(new AroundAdvice() {
            @Override
            public <T> Object invoke(Class<T> clazz, Method method, Object[] args, Supplier<Object> supplier) throws Exception {
                System.out.println("around before");
                Object result = supplier.get();
                System.out.println("around after");
                return result;
            }
        }).addAdvice(new AroundAdvice() {
            @Override
            public <T> Object invoke(Class<T> clazz, Method method, Object[] args, Supplier<Object> supplier) throws Exception {
                System.out.println("around before2");
                Object result = supplier.get();
                System.out.println("around after2");
                return result;
            }
        });
    
        ProxyFactory proxyFactory = new CglibProxyFactory();
    
        Sleep sleep1 = proxyFactory.generateProxy(sleep, advisor);
    
        sleep1.sleep(100);
    
    }
}
