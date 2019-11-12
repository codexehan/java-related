package codexe.han.leetcode.escapeplan;

public class escape104 {
    public int maxDepth(TreeNode root) {
        return dft(root);
    }

    public int dft(TreeNode node){
        if(node==null) return 0;
        int leftMax = dft(node.left);
        int rightMax = dft(node.right);
        return Math.max(leftMax, rightMax)+1;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}

/**
 Given a binary tree, find its maximum depth.

 The maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

 Note: A leaf is a node with no children.

 Example:

 Given binary tree [3,9,20,null,null,15,7],

 3
 / \
 9  20
 /  \
 15   7
 return its depth = 3.
 */
