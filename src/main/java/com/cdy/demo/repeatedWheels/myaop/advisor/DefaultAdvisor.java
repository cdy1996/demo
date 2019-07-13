package com.cdy.demo.repeatedWheels.myaop.advisor;

import com.cdy.demo.repeatedWheels.myaop.advice.*;
import com.cdy.demo.repeatedWheels.myaop.pointcut.ClassPointCut;
import com.cdy.demo.repeatedWheels.myaop.pointcut.MethodPointCut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 横切逻辑
 * Created by 陈东一
 * 2019/7/13 0013 13:24
 */
public class DefaultAdvisor implements Advisor {
    
    private ClassPointCut classPointCut;
    private List<MethodPointCut> methodPointCuts;
    private List<Advice> adviceList;
    
    public DefaultAdvisor(ClassPointCut classPointCut) {
        this.classPointCut = classPointCut;
        this.methodPointCuts = new ArrayList<>();
        this.adviceList = new ArrayList<>();
    }
    
    /**
     * 构建横切逻辑调用链
     * @param supplier
     * @return
     */
    public AdviceChain buildChain(Supplier<Object> supplier) {
        AdviceChain adviceChain = new AdviceChain(supplier);
        for (Advice advice : adviceList) {
            if (advice instanceof BeforeAdvice) {
                adviceChain.addBeforeAdvice((BeforeAdvice) advice);
            }
            if (advice instanceof AroundAdvice) {
                adviceChain.addAroundAdvice((AroundAdvice) advice);
            }
            if (advice instanceof AfterAdvice) {
                adviceChain.addAfterAdvice((AfterAdvice) advice);
            }
        }
        return adviceChain;
    }
    
    /**
     * 添加方法切点,如果为空则默认拦截全部方法
     * @param methodPointCut
     * @return
     */
    public DefaultAdvisor addMethodPointCut(MethodPointCut methodPointCut) {
        methodPointCuts.add(methodPointCut);
        return this;
    }
    
    /**
     *
     * @param advice
     * @return
     */
    public DefaultAdvisor addAdvice(Advice advice) {
        adviceList.add(advice);
        return this;
    }
    
    /**
     * 判断是否需要构造代理对象
     *
     * @param object
     * @return
     */
    public Boolean matchClass(Object object) {
        return classPointCut == null || classPointCut.match(object);
    }
    
    /**
     * 判断是否需要拦截
     *
     * @param method
     * @return
     */
    public Boolean matchMethod(Method method) {
        if (methodPointCuts.isEmpty()) return true;
        for (MethodPointCut methodPointCut : methodPointCuts) {
            Boolean match = methodPointCut.match(method);
            if (match) return true;
        }
        return false;
    }
}
