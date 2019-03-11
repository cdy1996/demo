package com.cdy.demo.java.thinkInJava;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 陈东一 on 2017/9/16 19:04
 */
public class Sets {
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result;
        if (a instanceof EnumSet) {
            result = ((EnumSet) a).clone();
        } else {
            result = new HashSet<T>(a);
        }
        result.contains(b);
        return result;
    }

    enum Student {
        ROBIN("robin"),
        HARRY("harry", 40),
        ROBBIE("robbie");
        String name;
        int age;

        private Student(String name) {
            this(name, 0);
        }

        private Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        System.out.println("EnumSet.noneOf");
        EnumSet<Student> set = EnumSet.noneOf(Student.class);
        set.add(Student.HARRY);
        set.add(Student.ROBBIE);
        set.add(Student.ROBIN);
        for (Student p : set)
            System.out.println(p);
        set.clear();
        System.out.println("EnumSet.allOf");
        set = EnumSet.allOf(Student.class);
        for (Student p : set)
            System.out.println(p);
        set.clear();
        System.out.println("EnumSet.Of one");
        set = EnumSet.of(Student.ROBIN);
        for (Student p : set)
            System.out.println(p);
        System.out.println("EnumSet.Of two");
        set = EnumSet.of(Student.ROBIN, Student.HARRY);
        for (Student p : set)
            System.out.println(p);
    }
}


