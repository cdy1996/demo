package com.cdy.demo.algorithm;

public class ZuoXuanZiFu {

    class Solution {
        public String reverseLeftWords(String s, int n) {
            String substring = s.substring(0, n);
            String substring2 = s.substring(n);
            return substring2 + substring;
        }
    }
}
