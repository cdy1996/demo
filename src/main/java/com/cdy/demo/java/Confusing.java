package com.cdy.demo.java;

public class Confusing {


    public Confusing(Object o){

    }

    public Confusing(double[] array){

    }

//    public Confusing(int[] array){
//
//    }

    public static void main(String[] args) {
        new Confusing(null);
        new Confusing(1);
    }

}
