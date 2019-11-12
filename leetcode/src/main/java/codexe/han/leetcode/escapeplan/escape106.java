package codexe.han.leetcode.escapeplan;

import java.util.HashMap;
import java.util.Map;

public class escape106 {
    private Map<Integer, Integer> inorderMap = new HashMap<>();
    public TreeNode buildTree(int[] inorder, int[] postorder) {
        for(int i =0; i<inorder.length; i++){
            inorderMap.put(inorder[i],i);
        }
        return build(postorder, postorder.length-1, 0, inorder.length-1);
    }
    public TreeNode build(int[] postorder, int rootIndex, int start, int end){
        if(start<=end){
            TreeNode node = new TreeNode(postorder[rootIndex]);
            int index = inorderMap.get(node.val);
            node.right = build(postorder, rootIndex-1, index+1, end);
        //    node.left = build(postorder, rootIndex-(end-index-1-1-1), start, index-1);
            node.left = build(postorder, rootIndex-(end-index)-1, start, index-1);
            return node;
        }
        return null;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
/**
 Given inorder and postorder traversal of a tree, construct the binary tree.

 Note:
 You may assume that duplicates do not exist in the tree.

 For example, given

 inorder = [9,3,15,20,7]
 postorder = [9,15,7,20,3]
 Return the following binary tree:

 3
 / \
 9  20
 /  \
 15   7
 */
