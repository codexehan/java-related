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
     * 每一个往上pop的过程，都需要计数，说明一个小数已经访问过了
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

/**
 Given a binary search tree, write a function kthSmallest to find the kth smallest element in it.

 Note:
 You may assume k is always valid, 1 ≤ k ≤ BST's total elements.

 Example 1:

 Input: root = [3,1,4,null,2], k = 1
 3
 / \
 1   4
 \
 2
 Output: 1
 Example 2:

 Input: root = [5,3,6,2,4,null,null,1], k = 3
 5
 / \
 3   6
 / \
 2   4
 /
 1
 Output: 3
 */