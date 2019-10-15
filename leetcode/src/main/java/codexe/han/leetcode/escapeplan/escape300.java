package codexe.han.leetcode.escapeplan;

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

    //二分查找 O(nlogn)
    //
    public int lengthOfLIS1(int[] nums) {
        int res = 0;
        int[] dp = new int[nums.length];

        return 0;
    }
}
