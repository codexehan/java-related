package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

/**
 * Input: [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 */
public class escape300 {
    //动态规划 O(n2)
    public int lengthOfLIS(int[] nums) {
        int res = 0;
        if(nums.length==0) return res;
        int[] dp = new int[nums.length];
        int max;
        for(int i=0;i<nums.length;i++){
            max= 1;
            for(int j=i;j>=0;j--){
                if(nums[j]<nums[i]){
                    max = Math.max(max,dp[j]+1);
                }
                else{
                    max = Math.max(max,1);
                }
            }
            dp[i] = max;
            res = Math.max(res,max);
        }
        return res;
    }

    public static void main(String[] args) {
        lengthOfLIS1(new int[]{10,9,2,5,3,7,101,18});
    }
    //二分查找 O(nlogn)
    //先不用二分查找，结合334考虑一下该问题，每次对数字n与int[] res从头开始比较，并对res[i]最小值赋值
    //res[]是有序的，为了优化速度，才用二分查找
    //二分查找，找到大于等于该数字的最小值。
    public static int lengthOfLIS1(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp,Integer.MAX_VALUE);
        int max = 0;
        for(int n : nums){
            int pos = binarySearch(dp,n);
            dp[pos] = n;
            max = Math.max(pos+1,max);
        }
        return max;
    }
    public static int binarySearch(int[] res, int target){
        int left = 0;
        int right = res.length-1;

        while(left<right){
            int mid = left+(right-left)/2;
            if(res[mid]==target) return mid;
            else if(target>res[mid]){
                left = mid+1;
            }
            else{
                right = mid;
            }
        }
        return left;
    }
}
