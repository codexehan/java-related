package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

/**
 * 动态规划
 */
public class escape279 {
    public int numSquares(int n) {
        int[] dp = new int[n+1];
        Arrays.fill(dp,Integer.MAX_VALUE);
        dp[0] = 0;
        int min = Integer.MAX_VALUE;
        for(int i=1;i<=n;i++){
            int j=1;
            while(i-j*j>=0){
                min = Math.min(min,dp[i-j*j]+1);
                j++;
            }
            dp[i] = min;
            min = Integer.MAX_VALUE;
        }
        return dp[n];
    }
}
