package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [-1,0,1,2,-1,-4]
 * [-4,-1,-1,0,1,2]
 */
public class escape15 {
    public static void main(String[] args) {
        int[] arr = new int[]{-1,0,1,2,-1,-4};
     //   arr = new int[]{0,0,0,0,0};
        threeSum(arr);

    }
    public static List<List<Integer>> threeSumError(int[] nums) {
        List<List<Integer>> resList = new ArrayList<>();
        Arrays.sort(nums);
        for(int i=0;i<nums.length-2;i++){
            while(i!=0&&nums[i]==nums[i-1]&&i<nums.length-2){
                i++;
            }
            if(i>=nums.length-2){
                break;
            }
            int n1 = nums[i];
            int sum = -n1;
            int l = i+1;
            int r = nums.length-1;
            while(l<r){
                while(l<r&&nums[l]+nums[r]<sum){
                    l++;
                }
                while(l<r&&nums[l]+nums[r]>sum){
                    r--;
                }
                while(l<r&&nums[l]+nums[r]==sum){
                    resList.add(Arrays.asList(n1,nums[l],nums[r]));
                    l++;
                    r--;
                    //error: 未检测0,0,2,2的情况
                }
            }
        }
        return resList;
    }



    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
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
        return res;
    }
}


