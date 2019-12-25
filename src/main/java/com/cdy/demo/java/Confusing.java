package com.cdy.demo.java;

public class Confusing {
    

    public Confusing(Object o){

    }

    public Confusing(double[] array){

    }

//    public Confusing(int[] array){
//
//    }

    
    
    static class ClassOne{}
    static class ClassTwo{}
    static interface InterfaceOne{}
    
    static <T extends ClassOne> T getClassOne(){return null;}
    static <T extends InterfaceOne> T getInterfaceOne(){
//        return (T)new InterfaceOne(){};
        return null;
    }
    
    static <T> void printType(T o) {
        System.out.println("genericPrint");
    }
   
    static <T extends ClassTwo> void printType(T o) {
        System.out.println("T is Class two");
    }
    
    public static void main(String[] args) {
//        new Confusing(null);
//        new Confusing(1);
    
    
        ClassOne classOne = getClassOne();
        InterfaceOne interfaceOne = getInterfaceOne();
        printType(getClassOne());
        printType(getInterfaceOne());
        printType(classOne);
        printType(interfaceOne);
    }

}

