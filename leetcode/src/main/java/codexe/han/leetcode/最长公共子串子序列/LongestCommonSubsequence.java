package codexe.han.leetcode.最长公共子串子序列;


public class LongestCommonSubsequence {
    public int lcs(String s, String q){
        int[][] dp = new int[s.length()][q.length()];
        //边界初始化问题，这边有一些问题，应该从i=1 j=1开始
        for(int i=0;i<s.length();i++){
            for(int j=0;j<q.length();j++){
                if(s.charAt(i)==q.charAt(j)){
                    dp[i][j] = dp[i-1][j-1]+1;
                }
                else{
                    dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }
        return dp[s.length()-1][q.length()-1];
    }
}
