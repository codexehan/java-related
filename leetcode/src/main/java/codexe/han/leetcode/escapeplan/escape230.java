package codexe.han.leetcode.escapeplan;

/**
 * 二叉树中序遍历，输出的是有序数组，由小到大
 */
public class escape230 {
    private int res;
    private int count;
    public int kthSmallest(TreeNode root, int k) {
        dfs(root,k);
        return res;
    }

    /**
     * 每一个往上pop的过程，都需要技术，说明一个小数已经访问过了
     */
    public void dfs(TreeNode node, int k){
        //左节点 和 根节点是最小的
        if(node.left!=null){
            dfs(node.left, k);
        }
        count++;
        if(k == count) res= node.val;
        if(node.right!=null) {
            dfs(node.right, k);
        }
    }

    public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
}
