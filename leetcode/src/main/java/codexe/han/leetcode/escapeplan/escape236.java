package codexe.han.leetcode.escapeplan;


/**
 * 搜寻p q所在的分支
 * 如果当前节点的左右分支分别包含pq，那么当前节点就是所要查找的节点。
 * 如果当前节点只有左分支或者右分支包含p或q，那么返回左分支，或者右分支
 */
public class escape236 {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //找到p和q节点 然后网上返回，当遇到某个节点的左右加点返回值都不为null的时候，返回root节点
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

/**
 Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.

 According to the definition of LCA on Wikipedia: “The lowest common ancestor is defined between two nodes p and q as the lowest node in T
 that has both p and q as descendants (where we allow a node to be a descendant of itself).”

 Given the following binary tree:  root = [3,5,1,6,2,0,8,null,null,7,4]




 Example 1:

 Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 Output: 3
 Explanation: The LCA of nodes 5 and 1 is 3.
 */
