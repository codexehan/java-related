package codexe.han.leetcode.escapeplan;

/**
 * 二分查找
 * 找到 最后一个 最大的小于target的数字
 * 找到 最后一个 最大的小于target+1的数字（为了用上面的的同一个function）
 */
public class escape34 {
    public int[] searchRange(int[] nums, int target) {
        int start = findLargestLessThan(0, nums.length-1, nums, target);
        if(nums.length==0 || !(/*start>=0 && */start<nums.length-1) || !(nums[start+1]==target)) return new int[]{-1,-1};//start可以小于0，在0位置就是target的时候
        return new int[]{start+1, findLargestLessThan(start, nums.length-1, nums, target+1)};

    }

    public int findLargestLessThan(int left, int right, int[] nums, int target){
        while(left<=right){
            int mid = (left+right)/2;
            if(nums[mid]==target){
                right = mid-1;
            }
            if(nums[mid]<target){
                left = mid+1;//left=mid, [1,2] target=2,会出现问题
            }
            if(nums[mid]>target){
                right = mid-1;
            }
        }
        return left-1;
    }

}
