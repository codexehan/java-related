package codexe.han.leetcode.escapeplan;

/**
 * 数组被分成左右两部分，每部分都是升序的
 * 所以重点是找到分隔的位置
 * 思路就是先找mid 判断mid和start (或者mid和end)之间的关系，从而确定哪一边是有序的
 * start<mid, 左边一定是升序，判断start,target, mid之间的关系，target不在这中间的话，就在右边，
 * 然后mid+1, end，在调用上述逻辑，找target
 *
 * 二分查找调用left = mid-1 right = mid+1，因为更新后会出现left==right的情况，所以while循环一定是left<=right
 */
public class escape33 {
    public int search(int[] nums, int target) {
        return binarySearch(0, nums.length-1, target, nums);
    }
    public int binarySearch(int left, int right, int target, int[] nums){
        while(left<=right){//right left 移动都是=mid,所以会出现left==right的情况，跳出
            int mid = (left+right)/2;
            if(nums[mid] == target) return mid;

            if(nums[left]<=nums[mid]){//确定mid所属的区间 是左半区还是右半区
                //左边是连续递增的
                if(nums[left]<= target && target < nums[mid]){
                    //target在这中间 -> 二分查找正常逻辑
                    right = mid-1;
                }
                else{
                    //target不在这中间
                    left = mid+1;
                }
            }
            else{
                //右边是连续的
                if(nums[mid]< target && target <= nums[right]){
                    //target在这中间 -> 二分查找正常逻辑
                    left = mid+1;
                }
                else{
                    //target不在这中间
                    right = mid-1;
                }
            }
        }
        return -1;
    }
}

/**
 Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.

 (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).

 You are given a target value to search. If found in the array return its index, otherwise return -1.

 You may assume no duplicate exists in the array.

 Your algorithm's runtime complexity must be in the order of O(log n).

 Example 1:

 Input: nums = [4,5,6,7,0,1,2], target = 0
 Output: 4
 Example 2:

 Input: nums = [4,5,6,7,0,1,2], target = 3
 Output: -1
 */


/*
Given an array of n distinct non-empty strings, you need to generate minimal possible abbreviations for every word following rules below.

Begin with the first character and then the number of characters abbreviated, which followed by the last character.
If there are any conflict, that is more than one words share the same abbreviation,
a longer prefix is used instead of only the first character until making the map from word to abbreviation become unique.
In other words, a final abbreviation cannot map to more than one original words.
If the abbreviation doesn't make the word shorter, then keep it as original.
Example:
Input: ["like", "god", "internal", "me", "internet", "interval", "intension", "face", "intrusion"]
Output: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
Note:
Both n and the length of each word will not exceed 400.
The length of each word is greater than 1.
The words consist of lowercase English letters only.
The return answers should be in the same order as the original array.

 */

class escape81{
    public static void main(String[] args) {
        //[2,5,6,0,0,1,2] 3
        //[1,3,1,1,1] 3
        getRes(new int[]{1,3,1,1,1},3);
    }
    public static boolean getRes(int [] nums, int target){
        int left = 0;
        int right = nums.length-1;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(nums[mid]==target) return true;
            if(nums[mid]==nums[right]){//右边区间都是相同的数字 搜索左边
                if(target<nums[mid]) {
                    return false;
                }
                else if(target > nums[mid]){
                    right = mid-1;//搜索左边
                }
            }
            else if(nums[mid]<nums[right]) {//右边是连续的递增区间
                if (nums[mid] < target && target <= nums[right]) {//右边是单调递增的
                    left = mid + 1;
                }
                else{
                    right = mid-1;
                }
            }
            else if(nums[mid]>nums[left]){//左边是连续的递增区间
                if(nums[left] <= target && target<nums[mid]){
                    right = mid-1;
                }
                else{
                    left = mid+1;
                }
            }
        }
        return false;
    }

}