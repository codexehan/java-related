package codexe.han.leetcode.回文问题;

/**
 Example 1:

 Input: "babad"
 Output: "bab"
 Note: "aba" is also a valid answer.
 Example 2:

 Input: "cbbd"
 Output: "bb"
 */
//动态规划 dp[i][j] 表示第ij个是不是构成了回文子串
//dp[i][j] = s.charAt(i)==s.charAt(j)&&(i==j-1 || i+1==j-1 || i==j||dp[i+1][j-1]);
public class leetcode5_LongestPalindromicSubstring {
    public static void main(String[] args) {
        longestPalindrome2("abcda");
    }
    public static String longestPalindrome(String s) {
        if(s.length()==0) return "";
        int l=0, r=0, maxScope = Integer.MIN_VALUE;
        boolean[][] dp = new boolean[s.length()][s.length()];
        for(int j=0;j<s.length();j++){
            for(int i=0;i<=j;i++){
                dp[i][j] = s.charAt(i)==s.charAt(j)&&(i==j-1||i+1==j-1||i==j||dp[i+1][j-1]);
                if(dp[i][j]){
                    if(j-i+1>maxScope){
                        maxScope = j-i+1;
                        l = i;
                        r = j;
                    }
                }
            }
        }
        return s.substring(l,r+1);
    }

    public static String longestPalindrome1(String s) {
        if(s.length()==0) return "";
        int l=0, r=0, maxScope = Integer.MIN_VALUE;
        boolean[][] dp = new boolean[s.length()][s.length()];
        for(int j=0;j<s.length();j++){
            for(int i=j;i>=0;i--){
                dp[i][j] = s.charAt(i)==s.charAt(j)&&(i==j-1||i+1==j-1||i==j||dp[i+1][j-1]);
                if(dp[i][j]){
                    if(j-i+1>maxScope){
                        maxScope = j-i+1;
                        l = i;
                        r = j;
                    }
                }
            }
        }
        return s.substring(l,r+1);
    }
    public static String longestPalindrome2(String s) {
        int max = 0;
        int left = 0;
        int[][] dp = new int[s.length()][s.length()];
        for(int j=0;j<s.length();j++){
            for(int i=j;i>=0;i--){
                if(s.charAt(i)==s.charAt(j)){
                    dp[i][j] = i==j?1:j-i==1?2:dp[i+1][j-1]+2;
                    if(dp[i][j]>max){
                        left = i;
                        max = dp[i][j];
                    }
                }
                else{
                    dp[i][j] = Math.max(dp[i+1][j],dp[i][j-1]);
                }
            }
        }
        System.out.println(left+" "+dp[0][s.length()-1]+" "+max);
        return s.substring(left,left+dp[0][s.length()-1]);
    }

}
