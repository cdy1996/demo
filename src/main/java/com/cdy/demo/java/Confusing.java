package com.cdy.demo.java;

import java.io.Serializable;

public class Confusing {


//    public Confusing(Object o){
//
//    }
//
//    public Confusing(double[] array){
//
//    }
//    public Confusing(int int1){
//
//    }

//    public Confusing(int[] array){
//
//    }

    class ClassOne {
    }

    class ClassTwo {
    }

    interface InterfaceOne {
    }


    static <T extends ClassOne> T getClassOne() {
        return null;
    }

    static ClassOne getClassOne1() {
        return null;
    }

    static <T extends InterfaceOne> T getInterfaceOne() {
        return null;
    }


    static InterfaceOne getInterfaceOne1() {
        return null;
    }


    static <T> void printType(T o) {
        System.out.println("genericPrint");
    }

//    static void printType(Object o) {
//        System.out.println("genericPrint");
//    }

    static <T extends ClassTwo > void printType(T o) {
        System.out.println("T is Class two");
    }


//    static void printType(InterfaceOne two) {
//        System.out.println("T is Class two");
//    }

//    static void printType(ClassTwo two) {
//        System.out.println("T is Class two");
//    }

    public static void main(String[] args) {
//        Object o = null;
//        new Confusing(o);
//        new Confusing(null);
//        new Confusing(1);


        ClassOne classOne = getClassOne();
        InterfaceOne interfaceOne = getInterfaceOne();
        printType(null);
        printType(getClassOne());
        printType(getInterfaceOne());
        printType(classOne);
        printType(interfaceOne);
    }

}

class Interval<T extends Comparable & Serializable> implements Serializable {
    private T lower;
    private T upper;

    public Interval(T first, T second) {
        if (first.compareTo(second) <= 0) {
            lower = first;
            upper = second;
        } else {
            lower = second;
            upper = first;
        }
    }
}
