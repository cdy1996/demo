package com.cdy.demo.structure;

import java.util.Arrays;

/**
 * dubbo 加权轮询算法实现
 * Created by 陈东一
 * 2020/3/29 0029 16:15
 */
public class RoundRobinDemo {
    
    public static void main(String[] args) {
        int[] weight = new int[]{1, 2};
        int[] curr = new int[]{0, 0};
        int[] count = new int[]{0, 0};
        for (int i = 0; i < 1000; i++) {
            int select = select(weight, curr, weight.length);
            count[select]++;
        }
        System.out.println(Arrays.toString(count));
    }
    
    private static int select(int[] weight, int[] curr, int length) {
        int min = Integer.MIN_VALUE;
        int total = 0;
        int pos = -1;
        for (int i = 0; i < length; i++) {
            curr[i] = curr[i] + weight[i];
            if (curr[i] > min) {
                min = curr[i];
                pos = i;
            }
            total += weight[i];
        }
        if (pos != -1) {
            curr[pos] -= total;
            return pos;
        } else {
            return -1;
        }
    }
}
