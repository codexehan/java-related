package codexe.han.leetcode.数字相加之和;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0? Find all unique triplets in the array which gives the sum of zero.

 Note:

 The solution set must not contain duplicate triplets.

 Example:

 Given array nums = [-1, 0, 1, 2, -1, -4],

 A solution set is:
 [
 [-1, 0, 1],
 [-1, -1, 2]
 ]
 */
public class leetcode15_3Sum {
    public static void main(String[] args) {
        threeSum(new int[]{0,0,0});
    }
    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for(int i=0;i<nums.length-2;i++){
            if(i!=0&&nums[i]==nums[i-1]) continue;
            int target = -nums[i];
            for(int l=i+1, h=nums.length-1;l<h;){
                //一定要先计算两数字之和
                if(nums[l]+nums[h]==target){
                    res.add(Arrays.asList(nums[i],nums[l],nums[h]));
                    l++;
                    h--;
                    //这个位置很重要
                    while(l<h&&nums[l]==nums[l-1]) l++;
                    while(l<h&&h!=nums.length-1&&nums[h]==nums[h+1]) h--;
                }
                else if(nums[l]+nums[h]<target){
                    l++;
                }
                else{
                    h--;
                }
            }
        }
        return res;



       /* List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i + 2 < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {              // skip same result
                continue;
            }
            int j = i + 1, k = nums.length - 1;
            int target = -nums[i];
            while (j < k) {
                if (nums[j] + nums[k] == target) {
                    res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    j++;
                    k--;
                    //避免[-2,0,0,2,2]，[0，2]的情况
                    while (j < k && nums[j] == nums[j - 1]) j++;  // skip same result
                    while (j < k && nums[k] == nums[k + 1]) k--;  // skip same result
                } else if (nums[j] + nums[k] > target) {
                    k--;
                } else {
                    j++;
                }
            }
        }
        return res;*/

    }

}
