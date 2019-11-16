package codexe.han.leetcode.回文问题;

import java.util.ArrayList;
import java.util.List;

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
//dp[i][j]=s[i]==s[j]?i-1==jdp[i+1][j-1]+2:Math.max(dp[i][j-1],dp[i+11][j]);
public class leetcode516_LongestPalindromicSubsequence {
    public static void main(String[] args) {
        System.out.println(longestPalindromeSubseq("euazbipzncptldueeuechubrcourfpftcebikrxhybkymimgvldiwqvkszfycvqyvtiwfckexmowcxztkfyzqovbtmzpxojfofbvwnncajvrvdbvjhcrameamcfmcoxryjukhpljwszknhiypvyskmsujkuggpztltpgoczafmfelahqwjbhxtjmebnymdyxoeodqmvkxittxjnlltmoobsgzdfhismogqfpfhvqnxeuosjqqalvwhsidgiavcatjjgeztrjuoixxxoznklcxolgpuktirmduxdywwlbikaqkqajzbsjvdgjcnbtfksqhquiwnwflkldgdrqrnwmshdpykicozfowmumzeuznolmgjlltypyufpzjpuvucmesnnrwppheizkapovoloneaxpfinaontwtdqsdvzmqlgkdxlbeguackbdkftzbnynmcejtwudocemcfnuzbttcoew"));
    }

    /**
     i-j子串内，i,j不相等，那么dp[i][j]=Math.max(dp[i][j-1],dp[i+11][j])
     */
    public static int longestPalindromeSubseq(String s) {
        //这个顺序是有问题的 第二层循环从外向内存在问题  应该是由内向外进行
        /**
         * 由外向内
            * * *
            | |
            |   |
              | |
         */
        int[][] dp = new int[s.length()][s.length()];
        for(int j=0;j<s.length();j++){
            for(int i=j;i>=0;i--){
                if(s.charAt(j) == s.charAt(i)){
                    dp[i][j]=i+1==j?2:i==j?1:dp[i+1][j-1]+2;
                }
                else{
                    dp[i][j]=i+1==j?1:Math.max(dp[i+1][j],dp[i][j-1]);//"asdbdh" i:2 j:5
                }
            }
        }
        return dp[0][s.length()-1];
    }
    public static int longestPalindromeSubseq1(String s) {
        int[][] dp = new int[s.length()][s.length()];

        for (int i = s.length() - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i+1; j < s.length(); j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i+1][j-1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
                }
            }
        }
        return dp[0][s.length()-1];
    }
}
