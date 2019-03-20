package com.cdy.demo.structure;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树
 */
public class TreeNode {
    public int order;
    public TreeNode left;
    public TreeNode right;
    public int deep;
    
    public TreeNode(int order) {
        this.order = order;
    }
    
    public TreeNode put(TreeNode root, TreeNode node) {
        if (root == null) {
            return null;
        }
        if (node.order < root.order) {
            if (put(root.left, node) == null) {
                node.deep = root.deep + 1;
                root.left = node;
            }
        } else if (node.order > root.order) {
            if (put(root.right, node) == null) {
                node.deep = root.deep + 1;
                root.right = node;
            }
        } else {
            node.deep = root.deep;
            root = node;
        }
        return root;
    }
    
    
    public TreeNode preOrder(TreeNode node) {
        if (node != null) {
            System.out.println(node.order);
            preOrder(node.left);
            preOrder(node.right);
        }
        return null;
    }
    
    public void preOrderNoRecursion(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        stack.add(null);
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                stack.add(root);
                System.out.println(root.order);
                root = root.left;
            } else {
                root = stack.pop();
                root = root.right;
            }
        }
    }
    
    public TreeNode middleOrder(TreeNode node) {
        
        if (node != null) {
            middleOrder(node.left);
            System.out.println(node.order);
            middleOrder(node.right);
        }
        return null;
    }
    
    public void middleOrderNoRecursion(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        stack.add(null);
        while (!stack.isEmpty() || root != null) {
            if (root != null) {
                stack.add(root);
                root = root.left;
            } else {
                root = stack.pop();
                System.out.println(root.order);
                root = root.right;
            }
        }
    }
    
    public TreeNode postOrder(TreeNode node) {
        
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            System.out.println(node.order);
        }
        return null;
    }
    
    
    public ArrayList<Integer> postOrderNoRecursion(TreeNode root) {
        ArrayList<Integer> list=new ArrayList<>();
        if(root==null)
            return list;
        Stack<TreeNode>stack=new Stack<>();//存储节点
        stack.push(root);
        while(!stack.isEmpty()){
            
            TreeNode node=stack.pop();
            list.add(0,node.order); //每次在队列的头部加入节点
            if(node.left!=null)
                stack.push(node.left);
            if(node.right!=null)
                stack.push(node.right);
        }
        return list;
        
    }

    
    /**
     * 广度优先
     *
     * @param root
     * @return
     */
    public ArrayList<Integer> hierarchyPriority(TreeNode root) {
        ArrayList<Integer> lists = new ArrayList<Integer>();
        if (root == null)
            return lists;
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode tree = queue.poll();
            if (tree.left != null)
                queue.offer(tree.left);
            if (tree.right != null)
                queue.offer(tree.right);
            lists.add(tree.order);
        }
        return lists;
    }
    
    /**
     * 深度优先  递归版本参见先序遍历
     *
     * @param root
     * @return
     */
    public ArrayList<Integer> deepPriority(TreeNode root) {
        ArrayList<Integer> lists = new ArrayList<Integer>();
        if (root == null)
            return lists;
        Stack<TreeNode> stack = new Stack<TreeNode>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode tree = stack.pop();
            //先往栈中压入右节点，再压左节点，这样出栈就是先左节点后右节点了。
            if (tree.right != null)
                stack.push(tree.right);
            if (tree.left != null)
                stack.push(tree.left);
            lists.add(tree.order);
        }
        return lists;
    }
    
    
    public static void main(String[] args) {
        TreeNode treeNode1 = new TreeNode(2);
        treeNode1.put(treeNode1, new TreeNode(1));
        treeNode1.put(treeNode1, new TreeNode(4));
        treeNode1.put(treeNode1, new TreeNode(3));
        treeNode1.put(treeNode1, new TreeNode(5));
        treeNode1.put(treeNode1, new TreeNode(6));
        
        treeNode1.preOrder(treeNode1);
        treeNode1.middleOrder(treeNode1);
        treeNode1.postOrder(treeNode1);
    }
    
}


