package codexe.han.leetcode.escapeplan;

/**
 * 左子树最大值小于node.val   右子树最小值大于node.val
 *
 * 由下往上做的问题在于，每次返回高度，需要确定该子书是左子树还是右子树,知道该使用maxLeft 还是 minRight
 */
public class escape98 {
    public boolean isValidBST(TreeNode root) {
        //root node.val为Integer.MAX_VALUE 或者 Integer.MIN_VALUE
        //所以要讲Integer.MAX_VALUE Integer.MIN_VALUE替换为Long
        return check(root, Long.MAX_VALUE, Long.MIN_VALUE);
    }

    /**
     * 由下往上做
     */
    /*public ValRes check(TreeNode node){
        if(node.left==null && node.left==null) return new ValRes();
        ValRes valResLeft = check(node.left);
        ValRes valResRight = check(node.right);//最大值永远都是Integer.MAX_VALUE
        if(valid) valid = (valResLeft.max<node.val) && (valResRight.min>node.val);
        int maxLeft = Math.max(valResRight.max, node.val);
        int minRight = Math.min(valResLeft.min, node.val);
        return new ValRes(maxLeft, minRight);
    }
    class ValRes{
        int max;
        int min;
        public ValRes(int max, int min){
            max = max;
            min = min;
        }
    }*/

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }


    public boolean check(TreeNode node, long max, long min){
        if(node == null) return true;
        if(node.val<=min || node.val>=max) return false;//如果有不符合条件的情况，就提前退出运算即可
        return check(node.left, node.val, min) && check(node.right, max, node.val);
    }
}
/**
 Given a binary tree, determine if it is a valid binary search tree (BST).

 Assume a BST is defined as follows:

 The left subtree of a node contains only nodes with keys less than the node's key.
 The right subtree of a node contains only nodes with keys greater than the node's key.
 Both the left and right subtrees must also be binary search trees.


 Example 1:

 2
 / \
 1   3

 Input: [2,1,3]
 Output: true
 */

