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
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
