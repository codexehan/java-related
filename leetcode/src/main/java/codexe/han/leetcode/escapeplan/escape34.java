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
/**
 Given an array of integers nums sorted in ascending order, find the starting and ending position of a given target value.

 Your algorithm's runtime complexity must be in the order of O(log n).

 If the target is not found in the array, return [-1, -1].

 Example 1:

 Input: nums = [5,7,7,8,8,10], target = 8
 Output: [3,4]
 Example 2:

 Input: nums = [5,7,7,8,8,10], target = 6
 Output: [-1,-1]
 */


class Demo{
    public int test(int[] nums, int target){
        int left = 0;
        int right = nums.length-1;
        //right will point to the result
        while(left<=right){
            int mid = left + (right-left)/2;
            if(nums[mid]<target){
                left = mid +1;
            }
            else{
                right = mid-1;
            }
        }
        return right;
    }
}