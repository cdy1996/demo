package com.cdy.demo.repeatedWheels.mycglib;

public class MethodProxy {
    private String methodName;
    private SampleBean$FastClass sampleBean$FastClass;


    public MethodProxy(String methodName, Class<SampleBean$FastClass> fastClass) {
        try {
            this.sampleBean$FastClass = fastClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.methodName = methodName;
    }

    public Object invokeSuper(Object o, Object[] objects) {
        return sampleBean$FastClass.invoke(o, methodName, objects);
    }
}
