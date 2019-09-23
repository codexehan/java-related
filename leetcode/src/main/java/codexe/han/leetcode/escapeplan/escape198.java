package codexe.han.leetcode.escapeplan;

/**
 * 不相邻的数相加之和最大
 * Input: [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
 *              Total amount you can rob = 2 + 9 + 1 = 12.
 */
public class escape198 {
    public int rob(int[] nums) {
        int[] dp = new int[nums.length+2];
        int max = 0;
        dp[0] = 0;
        dp[1] = 0;
        for(int i=0; i<nums.length;i++){
            dp[i+2] = dp[i] + nums[i+2];
            max = Math.max(dp[i+2], max);
        }
        return max;
    }
}
