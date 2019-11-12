package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class escape103 {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if(root==null) return res;
        deepFirstTravel(root, 0, res);
        return res;
    }

    public void deepFirstTravel(TreeNode node, int level, List<List<Integer>> res){
        if(level>=res.size()){
            res.add(new ArrayList<>());
        }
        if(level%2==0) {
            res.get(level).add(node.val);
        }
        else{
            res.get(level).add(0,node.val);
        }
        //保证一定是从左到右执行的
        if(node.left!=null) deepFirstTravel(node.left, level+1, res);
        if(node.right!=null) deepFirstTravel(node.right, level+1, res);
    }

    /**
     * 广度优先遍历太复杂了
     */
    /*public void breadFirstTravel(LinkedList<TreeNode> queue, List<List<Integer>> res, int direction){
        if(!queue.isEmpty()) {
            LinkedList<TreeNode> queueTmp = new LinkedList<>();
            List<Integer> resTmp = new ArrayList<>();
            TreeNode nodeTmp;
            while (!queue.isEmpty()) {
                *//*if(direction%2==0) nodeTmp = queue.pollLast();
                else nodeTmp = queue.pollFirst();*//*
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
    }*/

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
