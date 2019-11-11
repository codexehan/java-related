package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 广度优先遍历
 */
public class escape102 {
    public List<List<Integer>> levelOrder(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        if(root==null) return new ArrayList<>();//空节点
        queue.add(root);
        List<List<Integer>> res = new ArrayList<>();
        breadFirstTravel(queue, res);
        return res;
    }
    public void breadFirstTravel(LinkedList<TreeNode> queue, List<List<Integer>> res){
        if(!queue.isEmpty()) {
            LinkedList<TreeNode> queueTmp = new LinkedList<>();
            List<Integer> resTmp = new ArrayList<>();
            for (TreeNode nodeTmp : queue) {
                if (nodeTmp.left != null) queueTmp.add(nodeTmp.left);
                if (nodeTmp.right != null) queueTmp.add(nodeTmp.right);
                resTmp.add(nodeTmp.val);
            }
            res.add(resTmp);
            breadFirstTravel(queueTmp, res);
        }
    }

    public void deepFirstTravel(TreeNode node, int level, List<List<Integer>> res){
        if(level>=res.size()){
            res.add(new ArrayList<>());
        }
        res.get(level).add(node.val);
        if(node.left!=null) deepFirstTravel(node.left, level+1, res);
        if(node.right!=null) deepFirstTravel(node.right, level+1, res);
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}

/**
 Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).

 For example:
 Given binary tree [3,9,20,null,null,15,7],
 3
 / \
 9  20
 /  \
 15   7
 return its level order traversal as:
 [
 [3],
 [9,20],
 [15,7]
 ]
 */
