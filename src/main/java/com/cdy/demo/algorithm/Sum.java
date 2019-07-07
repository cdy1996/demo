package com.cdy.demo.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * 查找有序数组中是否存在一堆数字相加等于给定值
 */
public class Sum {
    
    /**
     * 查找有序数组中是否存在一堆数字相加等于给定值
     *
     * @param data 有序数组
     * @param sum  给定值
     * @return 是否存在
     */
    public static boolean find(int[] data, int sum) {
        Map<Integer, Integer> set = new HashMap<>();
        for (int datum : data) {
            set.put(datum, set.getOrDefault(datum, 0) + 1);
        }
        for (int d : data) {
            Integer temp = set.getOrDefault(sum - d, 0);
            if (temp >= 1) {
                return sum - d != d || temp > 1;  //在差值和d相等时检查是否数组中有两个以上d
            }
        }
        return false;
    }
    
    
    public static void main(String[] args) {
        System.out.println(find(new int[]{1, 2, 3, 9}, 8));
        System.out.println(find(new int[]{1, 3, 4, 5}, 8));
        System.out.println(find(new int[]{1, 1}, 2));
        System.out.println(find(new int[]{1}, 1));
        System.out.println(find(new int[]{}, 1));
    }
}
