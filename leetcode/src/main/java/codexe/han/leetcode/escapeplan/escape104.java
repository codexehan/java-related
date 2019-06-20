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
