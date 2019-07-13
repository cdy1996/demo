package com.cdy.demo.repeatedWheels.myaop.pointcut;

/**
 * 类切点
 * Created by 陈东一
 * 2019/7/13 0013 13:26
 */
public class ClassPointCut<T> implements PointCut{
    private Class<T> clazz;
    
    public ClassPointCut(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    public Class<T> getClazz() {
        return clazz;
    }
    
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public Boolean match(Object object){
        if (object.getClass().equals(clazz)) return true;
        for (Class<?> anInterface : object.getClass().getInterfaces()) {
            if (anInterface.equals(clazz)) return true;
        }
        return false;
    }
}
