package com.cdy.demo.algorithm;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 取n个有序数组的最大前k个
 */
public class TopK {
    
    static class Node {
        Long data;
        int x; //第几个数组
        int y; // 数组中的哪一位
        public Node(Long data, int x, int y) {
            this.data = data;
            this.x = x;
            this.y = y;
        }
    }
    
    public static long[] topn(long[][] array, int k) {
        if ( k==0 ) return new long[0];
        int total=0;
        //大根堆
        PriorityQueue<Node> queue = new PriorityQueue<>(k, (a, b) -> b.data.compareTo(a.data));
        // 塞入每个数组最后一个
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i].length <=0) continue;
            queue.add(new Node(array[i][array[i].length - 1], i, array[i].length - 1));
            total+=array[i].length;
        }
        // 取出第一个,然后放入一个取出元素所属的数组的上一个元素
        int i1 = k > total ? total : k;
        long[] result = new long[i1];
        for (int i = 0, j = 0; i < i1; i++) {
            Node node = queue.poll();
            if (node == null) continue;
            int y = --node.y;
            if (y >= 0) {
                queue.add(new Node(array[node.x][y], node.x, y));
            }
            result[j++] = node.data;
        }
        return result;
    }
    
    
    public static void main(String[] args) {
        System.out.println(Arrays.toString(topn(new long[][]{{1, 2, 3}, {7, 8, 9}, {4, 5, 6}}, 5)));
        System.out.println(Arrays.toString(topn(new long[][]{{1, 2, 3}, {7, 8, 9, 10}, {4, 5, 6}}, 5)));
        System.out.println(Arrays.toString(topn(new long[][]{{}, {}, {}}, 5)));
        System.out.println(Arrays.toString(topn(new long[][]{{}, {}, {}}, 0)));
    }
}
