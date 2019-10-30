package codexe.han.leetcode.回文问题;

/**
 Input:

 "bbbab"
 Output:
 4
 One possible longest palindromic subsequence is "bbbb".
 Example 2:
 Input:

 "cbbd"
 Output:
 2
 */
//dp[i][j]存储i~j之间的最长的回文子序列
//dp[i][j]=s[i]==s[j]?i-1==jdp[i+1][j-1]+1:dp[i+1][j-1]
public class leetcode516_LongestPalindromicSubsequence {
    public static void main(String[] args) {
        System.out.println(longestPalindromeSubseq("cbbd"));
    }
    public static int longestPalindromeSubseq(String s) {
        int[][] dp = new int[s.length()][s.length()];
        for(int j=0;j<s.length();j++){
            for(int i=0;i<=j;i++){
                if(s.charAt(j) == s.charAt(i)){
                    dp[i][j]=i+1==j?2:i==j?1:dp[i+1][j-1]+2;
                }
                else{
                    dp[i][j]=i+1==j?1:dp[i][j-1];//dp[i][j]=i+1==j?1:dp[i+1][j-1] 不对 必须计算i-j-1的最长子序列
                }
                dp[0][j] = Math.max(dp[0][j],dp[i][j]);
     //           System.out.println("i:"+i+" j:"+j+" dp value is "+dp[i][j]);
            }
        }
        return dp[0][s.length()-1];
    }
}
