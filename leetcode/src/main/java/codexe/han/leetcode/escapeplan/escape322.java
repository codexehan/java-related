package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

//dp[i] = Math.min(dp[i-coins[j]]+1,dp[i]);
public class escape322 {
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount+1];
        Arrays.fill(dp, amount+1);
        dp[0] = 0;
        for(int i=1;i<=amount;i++){
            for(int j=0;j<coins.length;j++){
                int tmp = i-coins[j];
                if(tmp>=0){
                    dp[i] = Math.min(dp[tmp]+1,dp[i]);
                }
            }
        }
        return dp[amount]==amount+1?-1:dp[amount];
    }
}
