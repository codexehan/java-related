package codexe.han.leetcode.escapeplan;

/**
 * 将该树想象成两棵树，分别进行左右对比
 */
public class escape101 {
    public boolean isSymmetric(TreeNode root) {
        return check(root, root);
    }

    public boolean check(TreeNode n1, TreeNode n2){
        if(n1==null && n2==null) return true;
        if(n1!=null&&n2!=null) {
            boolean res1 = check(n1.left, n2.right);//对称节点的做左右节点是否对称
            boolean res2 = check(n1.right, n2.left);//对称节点的做右左节点是否对称
            return n1.val==n2.val&&res1&&res2;
        }
        return false;
    }

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
/**
 Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).

 For example, this binary tree [1,2,2,3,4,4,3] is symmetric:

 1
 / \
 2   2
 / \ / \
 3  4 4  3

 But the following [1,2,2,null,3,null,3] is not:

 1
 / \
 2   2
 \   \
 3    3
 */
