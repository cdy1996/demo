package com.cdy.demo.repeatedWheels.mycglib;

public class SampleBean$FastClass {


    public Object invoke(Object o, String name, Object[] params) {
        SampleBean$ sampleBeanChild = (SampleBean$) o;
        switch (name) {
            case "getValue":
                return sampleBeanChild.getValue$();
            case "setValue":
                sampleBeanChild.setValue$((String)params[0]);
            default:
                return null;
        }
    }
}