package com.cdy.demo.repeatedWheels.myaop.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * todo
 * Created by 陈东一
 * 2019/7/13 0013 13:58
 */
public class AdviceChain {
    
    private List<BeforeAdvice> beforeAdvices;
    private List<AroundAdvice> aroundAdvices;
    private List<AfterAdvice> afterAdvices;
    private Supplier<Object> supplier;
    
    public AdviceChain( Supplier<Object> supplier) {
        this.beforeAdvices = new ArrayList<>();
        this.aroundAdvices =new ArrayList<>();
        this.afterAdvices =new ArrayList<>();
        this.supplier = supplier;
    }
    
    public void addBeforeAdvice(BeforeAdvice beforeAdvice){
        beforeAdvices.add(beforeAdvice);
    }
    public void addAroundAdvice(AroundAdvice aroundAdvice){
        aroundAdvices.add(aroundAdvice);
    
    }
    public void addAfterAdvice(AfterAdvice afterAdvice){
        afterAdvices.add(afterAdvice);
    }
    
    public <T>Object doInvoke(Class<T> clazz, Method method, Object[] objects){
    
        beforeAdvices.forEach(e->e.invokeBefore(clazz, method, objects));
    
        Object object = null;
        Exception exception = null;
        Supplier<Object> finalObjectSupplier = supplier;
        for (AroundAdvice aroundAdvice : aroundAdvices) {
            Supplier<Object> temp = finalObjectSupplier;
            finalObjectSupplier = () -> {
                try {
                    return aroundAdvice.invoke(clazz, method, objects, temp);
                } catch (Exception e) {
                    return e;
                }
            };
        }
        Object result = finalObjectSupplier.get();
        if (result instanceof Exception) {
            exception = (Exception) result;
        } else {
            object = result;
        }
    
        Exception finalException = exception;
        Object finalObject = object;
        afterAdvices.forEach(e->e.invokeAfter(clazz, method, objects, finalObject, finalException));
        
        return object;
    }
}
