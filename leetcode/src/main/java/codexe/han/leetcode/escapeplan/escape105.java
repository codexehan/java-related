package codexe.han.leetcode.escapeplan;

import java.util.HashMap;
import java.util.Map;

public class escape105 {
    private Map<Integer, Integer> inorderMap = new HashMap<>();
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        for(int i=0; i<inorder.length;i++){
            inorderMap.put(inorder[i],i);
        }
        return build(preorder, 0,0,inorder.length-1);
    }

    public TreeNode build(int[] preorder,int rootIndex, int start, int end){
        if(start<=end) {
            int val = preorder[rootIndex];
            TreeNode node = new TreeNode(val);
            int index = inorderMap.get(val);
            node.left = build(preorder, rootIndex + 1, start, index - 1);
       //     node.right = build(preorder, rootIndex + (index-1 - start +1 +1), index + 1, end);
            node.right = build(preorder, rootIndex + (index-1 - start +1 +1), index + 1, end);
            return node;
        }
        return null;
    }

   /* private int in=0;
    private int pre=0;
    private TreeNode helper(int[] preorder, int[] inorder, int rootval) {
        if (in == inorder.length || inorder[in] == rootval)
            return null;

        TreeNode root = new TreeNode(preorder[pre++]);
        root.left = helper(preorder, inorder, root.val);
        in++;
        root.right = helper(preorder, inorder, rootval);
        return root;
    }*/
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}
/**
 Given preorder and inorder traversal of a tree, construct the binary tree.

 Note:
 You may assume that duplicates do not exist in the tree.

 For example, given

 preorder = [3,9,20,15,7]
 inorder = [9,3,15,20,7]
 Return the following binary tree:

 3
 / \
 9  20
 /  \
 15   7
 */
