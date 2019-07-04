package com.cdy.demo;

import java.util.HashSet;
import java.util.Set;

/**
 * 查找有序数组中是否存在一堆数字相加等于给定值
 */
public class Sum {

    /**
     * 查找有序数组中是否存在一堆数字相加等于给定值
     *
     * @param data 有序数组
     * @param sum  给定值
     * @return  是否存在
     */
    public static boolean find(int[] data, int sum) {
        Set<Integer> set = new HashSet<>();
        for (int datum : data) {
            set.add(datum);
        }
        for (int d : data) {
            if (set.contains(sum - d)) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(find(new int[]{1, 2, 3, 9}, 8));
        System.out.println(find(new int[]{1, 2, 4, 5}, 8));
    }
}
