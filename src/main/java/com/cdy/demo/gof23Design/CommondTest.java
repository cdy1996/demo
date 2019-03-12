package com.cdy.demo.gof23Design;

import com.netflix.hystrix.*;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CommondTest {


    public static void main(String[] args) throws InterruptedException {
        Person person = new Person();
        ElectricLight dd = new ElectricLight();
        person.open(dd);
        person.close(dd);


        Commond on = new LightOnCommond(dd);
        Commond off = new LightOffCommond(dd);
        person.action(on);
        person.action(off);

        Thread.sleep(Integer.MAX_VALUE);
    }


}


interface Commond {
    void execute();
}

class LightOnCommond implements Commond {

    ElectricLight light;

    public LightOnCommond(ElectricLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.open();
    }
}

class LightOffCommond implements Commond {

    ElectricLight light;

    public LightOffCommond(ElectricLight light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.close();
    }
}

class Person {
    public void open(ElectricLight dd) {
        dd.status = true;
    }

    public void close(ElectricLight dd) {
        dd.status = false;
    }

    public void action(Commond commond) {
        commond.execute();
    }

}

class ElectricLight {
    boolean status = false;

    public void open() {
        status = true;
    }

    public void close() {
        status = false;
    }

}

