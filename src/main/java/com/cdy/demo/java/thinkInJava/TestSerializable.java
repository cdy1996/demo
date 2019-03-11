package com.cdy.demo.java.thinkInJava;

import java.io.*;

/**
 * Created by 陈东一 on 2017/9/24 11:30
 */
public class TestSerializable {
    static class A implements Serializable {
        String a;

        public A() {
            System.out.println("默认构造函数");

        }

        public A(String a) {
            this.a = a;
            System.out.println("构造函数");
        }

        @Override
        public String toString() {
            return a;
        }
    }

    static class B implements Externalizable {
        String b;
        String bb;

        public B() {
            System.out.println("默认构造函数");
        }

        public B(String b) {
            this.b = b;
            System.out.println("构造函数");
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(b);
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            b = (String) in.readObject();
        }

        @Override
        public String toString() {
            return b;
        }
    }

    static class C implements Serializable {
        private String c;
        private transient String cc;

        public C() {
            System.out.println("默认构造函数");
        }

        public C(String c) {
            this.c = c;
            this.cc = c + c;
            System.out.println("构造函数");
        }

        private void writeObject(ObjectOutputStream stream) throws IOException {
            System.out.println("writeObject");
            stream.defaultWriteObject();
            stream.writeObject(cc);
        }

        private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            System.out.println("readObject");
            stream.defaultReadObject();
            this.cc = (String) stream.readObject();
        }

        @Override
        public String toString() {
            return c + "\n" + cc;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        C a = new C("a");
        System.out.println(a);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("thinkInJava.out"));
        objectOutputStream.writeObject(a);
        objectOutputStream.close();
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("thinkInJava.out"));
        C aa = (C) objectInputStream.readObject();
        System.out.println(aa);
    }

}
