package codexe.han.leetcode.escapeplan;

public class escape110 {
    boolean isBalanced = true;
    public boolean isBalanced(TreeNode root) {
        check(root);
        return isBalanced;
    }
    public int check(TreeNode node){
        if(node == null) return 0;
        int heightL = check(node.left);
        int heightR = check(node.right);
        if(isBalanced) isBalanced = Math.abs(heightL-heightR)<=1;
        return Math.max(heightL, heightR)+1;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
