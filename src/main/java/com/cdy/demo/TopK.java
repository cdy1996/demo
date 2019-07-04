package com.cdy.demo;

import java.util.Comparator;
import java.util.PriorityQueue;

public class TopK {


    public static long[] topn(long[][] array, int k) {
        //大根堆
        PriorityQueue<Long> queue = new PriorityQueue<>(k, Comparator.reverseOrder());
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < array[i].length; j++) {
                queue.add(array[i][j]);
            }
        }
        long[] longs = new long[k];
        for (int i = 0; i < longs.length; i++) {
            if (queue.peek()!=null) {
                longs[i] = queue.poll();
            }
        }
        return longs;
    }


    public static void main(String[] args) {
//        topn(new long[][]{[],[],[]}, int k)
    }
}
