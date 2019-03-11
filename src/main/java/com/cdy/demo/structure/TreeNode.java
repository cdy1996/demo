package com.cdy.demo.structure;

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
            if (put(root.left, node)==null) {
                node.deep = root.deep+1;
                root.left = node;
            }
        } else if (node.order > root.order) {
            if (put(root.right, node)==null) {
                node.deep = root.deep+1;
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
        }
        if (node != null) {
            preOrder(node.left);
        }
        if (node != null) {
            preOrder(node.right);
        }
        return null;
    }

    public TreeNode middleOrder(TreeNode node) {

        if (node != null) {
            middleOrder(node.left);
        }
        if (node != null) {
            System.out.println(node.order);
        }
        if (node != null) {
            middleOrder(node.right);
        }
        return null;
    }

    public TreeNode postOrder(TreeNode node) {

        if (node != null) {
            postOrder(node.left);
        }
        if (node != null) {
            postOrder(node.right);
        }
        if (node != null) {
            System.out.println(node.order);
        }
        return null;
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


