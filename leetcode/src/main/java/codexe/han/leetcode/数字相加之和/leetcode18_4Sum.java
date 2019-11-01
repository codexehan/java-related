package codexe.han.leetcode.数字相加之和;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 Given an array nums of n integers and an integer target, are there elements a, b, c, and d in nums such that a + b + c + d = target? Find all unique quadruplets in the array which gives the sum of target.

 Note:

 The solution set must not contain duplicate quadruplets.

 Example:

 Given array nums = [1, 0, -1, 0, -2, 2], and target = 0.

 A solution set is:
 [
 [-1,  0, 0, 1],
 [-2, -1, 1, 2],
 [-2,  0, 0, 2]
 ]
 */
public class leetcode18_4Sum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for(int i=0;i<nums.length-3;i++){
            if(i!=0&&nums[i]==nums[i-1]) continue;//每一层 以相同数字开头的要过滤掉
            for(int j=i+1;j<nums.length-2;j++){
                if(j!=i+1&&nums[j]==nums[j-1]) continue;
                int l=j+1,h=nums.length-1;
                while(l<h){
                    if(nums[i]+nums[j]+nums[l]+nums[h]==target){
                        res.add(Arrays.asList(nums[i],nums[j],nums[l],nums[h]));
                        l++;
                        h--;
                        while(l<h&&nums[l]==nums[l-1]) l++;
                        while(l<h&&nums[h]==nums[h+1]) h--;
                    }
                    else if(nums[i]+nums[j]+nums[l]+nums[h]>target){
                        h--;
                    }
                    else{
                        l++;
                    }
                }
            }
        }
        return res;
    }
}
