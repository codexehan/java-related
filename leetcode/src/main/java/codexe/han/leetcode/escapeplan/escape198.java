package codexe.han.leetcode.escapeplan;

/**
 * 不相邻的数相加之和最大  但是  中间有可能横跨多个相加数值最大
 * 比如[2,1,1,2]
 * 所以要维护该节点之前的所有节点中的最大值
 *
 * Input: [2,7,9,3,1]
 * Output: 12
 * Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
 *              Total amount you can rob = 2 + 9 + 1 = 12.
 */
public class escape198 {
    public int rob(int[] nums) {
        int[] dp = new int[nums.length+2];
        int max = 0;
        int subMax = 0;
        dp[0] = 0;
        dp[1] = 0;
        for(int i=0; i<nums.length;i++){
            dp[i+2] = subMax+nums[i];
            max = Math.max(dp[i+2], max);
            subMax = Math.max(subMax,dp[i+1]);
        }
        return max;
    }
}
