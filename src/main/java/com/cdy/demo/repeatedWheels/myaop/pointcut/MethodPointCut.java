package com.cdy.demo.repeatedWheels.myaop.pointcut;

import java.lang.reflect.Method;

/**
 * 类切点
 * Created by 陈东一
 * 2019/7/13 0013 13:26
 */
public class MethodPointCut implements PointCut {
    private Method method;
    
    public MethodPointCut(Method method) {
        this.method = method;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public void setMethod(Method method) {
        this.method = method;
    }
    
    @Override
    public Boolean match(Object object) {
        return method.equals(object);
    }
}
