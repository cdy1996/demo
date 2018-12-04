package com.hexin.demo;

import java.io.UnsupportedEncodingException;

public class EncoderTest {


    public static void main(String[] args) throws UnsupportedEncodingException {
       /* String s = "肥蛇";
        String encode = URLEncoder.encode(s,"utf-8");
        String dencode = URLDecoder.decode(s,"utf-8");
        String s2 = new String(s.getBytes("utf-8"), "iso-8859-1");
        String s1 = new String(s2.getBytes("iso-8859-1"), "utf-8");
        System.out.println(encode);
        System.out.println(dencode);
        System.out.println(s2);
        System.out.println(s1);*/


        Abc abc = new Abc();
        abc.println("!23");

        aaa aaa = new EncoderTest().new aaa();
    }

    static class Abc {
        public void println(String abc) {
            System.out.println("123");
        }
    }
    class aaa {
        public void println(String abc) {
            System.out.println("123");
        }
    }
}
