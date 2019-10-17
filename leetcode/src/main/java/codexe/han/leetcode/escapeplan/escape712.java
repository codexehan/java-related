package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

//求出最长公共子序列
//i==j i和j一定是i-1 j-1的最长公共子序列的最后一个字符
public class escape712 {
    public int minimumDeleteSum(String s1, String s2) {
        int[][] dp = new int[s1.length()+1][s2.length()+1];//i,j的最长公共子序列
        dp[0][0] = 0;
        for(int i=1;i<s1.length()+1;i++){
            dp[i][0] = dp[i-1][0]+s1.charAt(i-1);
        }
        for(int i=1;i<s2.length()+1;i++){
            dp[0][i] = dp[0][i-1]+s2.charAt(i-1);
        }
        for(int i=1;i<s1.length()+1;i++){
            for(int j=1;j<s2.length()+1;j++){
                if(s1.charAt(i-1)==s2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                }
                else{
                    dp[i][j] = Math.min(dp[i-1][j]+s1.charAt(i-1),dp[i][j-1]+s2.charAt(j-1));
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}
