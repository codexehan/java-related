package codexe.han.leetcode.escapeplan;

/**
 * 为保证左右字数高度最大为1
 * 找数据中间值，划分，左右区间，数字数目最多差1个，递归执行
 */
public class escape108 {
    public TreeNode sortedArrayToBST(int[] nums) {
        return build(nums, 0, nums.length-1);
    }
    public TreeNode build(int[] nums, int left, int right){
        while(left<=right){
            int mid = (left+right)/2;
            TreeNode node = new TreeNode(nums[mid]);
            node.left = build(nums, left, mid-1);
            node.right = build(nums,mid+1, right);
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
