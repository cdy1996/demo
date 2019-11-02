package com.cdy.demo.repeatedWheels.mycglib;


import java.lang.reflect.Method;

public class SampleBean$ extends SampleBean {

    final MethodInterceptor methodInterceptor;

    private static Method getValue;
    private static MethodProxy getValueProxy;
    private static Method setValue;
    private static MethodProxy setValueProxy;


    static {

        try {
            getValue = SampleBean.class.getMethod("getValue");
            getValueProxy = new MethodProxy("getValue", SampleBean$FastClass.class);
            setValue = SampleBean.class.getMethod("setValue", String.class);
            setValueProxy = new MethodProxy("setValue", SampleBean$FastClass.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public SampleBean$(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }


    public String getValue$() {
        return super.getValue();
    }

    @Override
    public String getValue() {
        return (String) methodInterceptor.intercept(this, getValue, new Object[]{}, getValueProxy);
    }

    public void setValue$(String value) {
        super.setValue(value);
    }

    @Override
    public void setValue(String value) {
        methodInterceptor.intercept(this, setValue, new Object[]{value}, setValueProxy);
    }
}