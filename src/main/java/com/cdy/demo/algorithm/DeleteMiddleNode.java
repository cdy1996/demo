package com.cdy.demo.algorithm;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) { val = x; }
 * }
 */
//https://leetcode-cn.com/problems/delete-middle-node-lcci/
public class DeleteMiddleNode {

    class Solution {
        public void deleteNode(ListNode node) {
            ListNode pre = node;
            ListNode cur = node.next;
            while (true) {

                if (cur.next == null) {
                    pre.val = cur.val;
                    pre.next = null;
                    return;
                }

                pre.val = cur.val;
                pre = cur;
                cur = cur.next;
            }

        }
    }

}
