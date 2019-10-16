package codexe.han.leetcode.最长公共子串子序列;

public class LongestCommonSubstring {
    public int lcs(String s, String q){
        int max = 0;
        int[][]dp = new int[s.length()][q.length()];
        for(int i=0;i<s.length();i++){
            for(int j=0;j<q.length();j++){
                if(s.charAt(i)==q.charAt(j)){
                    dp[i][j]=dp[i-1][j-1]+1;
                    max = Math.max(max,dp[i][j]);
                }
                else{
                    dp[i][j] = 0;
                }
            }
        }
        return max;
    }
}
