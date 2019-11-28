package com.cdy.demo.java;

import java.io.UnsupportedEncodingException;

/**
 * todo
 * Created by 陈东一
 * 2019/11/10 0010 19:35
 */
public class CharTest {
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        
        String s = "\347\272\277\344\270\212\351\227\256\351\242\230\346\216\222\346\237\245";
        System.out.println(getOct(s));
        
        String strUtf8 = "\u4e2d\u56fd\u4f01\u4e1a\u5bb6\u6742\u5fd7";
        String strChinese = null;
        
        try {
            strChinese = new String(strUtf8.getBytes("UTF-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            
            strChinese = "decode error";
        }
        
        System.out.println(strChinese);
    }
    
    public static String toOct(String s) {
        String result = "";
        byte[] bytes = s.getBytes();
        for (byte b : bytes) {
            int b1 = b;
            if (b1 < 0) b1 = 256 + b1;
            result += "\\" + (b1 / 64) % 8 + "" + (b1 / 8) % 8 + "" + b1 % 8;
        }
        return result;
    }
    
    public static String getOct(String s) throws UnsupportedEncodingException {
        String[] as = s.split("\\\\");
        byte[] arr = new byte[as.length - 1];
        for (int i = 1; i < as.length; i++) {
            int sum = 0;
            int base = 64;
            for (char c : as[i].toCharArray()) {
                sum += base * ((int) c - '0');
                base /= 8;
            }
            if (sum >= 128) sum = sum - 256;
            arr[i - 1] = (byte) sum;
        }
        return new String(arr, "gbk"); //如果还有乱码，这里编码方式你可以修改下，比如试试看unicode gbk等等
    }
}
