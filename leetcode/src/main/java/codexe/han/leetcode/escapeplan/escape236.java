package codexe.han.leetcode.escapeplan;


/**
 * 搜寻p q所在的分支
 * 如果当前节点的左右分支分别包含pq，那么当前节点就是索要查找的节点。
 * 如果当前节点只有左分支或者右分支包含p或q，那么返回左分支，或者右分支
 */
public class escape236 {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root==null||root==p||root==q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        return left==null?right:right==null?left:root;
    }
    public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
}
