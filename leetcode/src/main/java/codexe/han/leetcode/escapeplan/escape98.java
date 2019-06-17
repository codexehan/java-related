package codexe.han.leetcode.escapeplan;

/**
 * 左子树最大值小于node.val   右子树最小值大于node.val
 *
 * 由下往上做的问题在于，每次返回高度，需要确定该子书是左子树还是右子树,知道该使用maxLeft 还是 minRight
 */
public class escape98 {
    boolean valid = true;
    public boolean isValidBST(TreeNode root) {
        return check(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
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


    public boolean check(TreeNode node, int max, int min){
        if(node == null) return true;
        if(node.val<=min || node.val>=max) return false;//如果有不符合条件的情况，就提前退出运算即可
        return check(node.left, node.val, min) && check(node.right, max, node.val);
    }
}

