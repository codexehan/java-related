package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class escape103 {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        List<List<Integer>> res = new ArrayList<>();
        breadFirstTravel(queue, res, 0);
        return null;
    }
    public void breadFirstTravel(LinkedList<TreeNode> queue, List<List<Integer>> res, int direction){
        if(!queue.isEmpty()) {
            LinkedList<TreeNode> queueTmp = new LinkedList<>();
            List<Integer> resTmp = new ArrayList<>();
            TreeNode nodeTmp;
            while (!queue.isEmpty()) {
                /*if(direction%2==0) nodeTmp = queue.pollLast();
                else nodeTmp = queue.pollFirst();*/
                nodeTmp = queue.pollFirst();
                if(direction%2==0){
                    queueTmp.addFirst(nodeTmp.left);
                    queueTmp.addFirst(nodeTmp.right);
                }
                else{
                    queueTmp.addLast(nodeTmp.left);
                    queueTmp.addLast(nodeTmp.right);
                }
                resTmp.add(nodeTmp.val);
            }
            res.add(resTmp);
            breadFirstTravel(queueTmp, res, direction+1);
        }
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
