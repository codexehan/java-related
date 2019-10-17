package codexe.han.leetcode.escapeplan;

/**
 * Example 1:
 *
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 *
 */
public class escape5 {

    private int maxLength;
    private int left;
    public String longestPalindrome(String s) {
        if(s.length() < 2){
            return s;
        }
        for(int i=0; i<s.length()-1;i++){
            getLength(s,i,i);
            getLength(s,i,i+1);
        }
        return s.substring(left, left+maxLength);
    }
    public void getLength(String s, int l, int r){
        while(l>=0 && r<s.length() && s.charAt(l)==s.charAt(r)){
            l--;
            r++;
        }
        if(maxLength < r-l-1){
            maxLength = r-l-1;
            left = l+1;
        }
    }

    //动态规划
    //dp[i][j] = Math.max(dp[i+1][j-1]+2,dp[i][j]) 因为substring连续的，如果是subsequence就不需要
    //
    public static String longestPalindrome1(String s) {
        int left=0;
        int max=1;
        if(s.length() < 2){
            return s;
        }
        int[][] dp = new int[s.length()][s.length()];
        for(int i=0;i<s.length();i++){
            dp[i][i] = 1;
        }
        for(int j=1;j<s.length();j++){
            int h = j;
            for(int i=0;i<h;i++){
                if(s.charAt(i)==s.charAt(j)){
                    if(i+1==h||dp[i+1][h-1]!=0) {//判断条件保证连续性
                        dp[i][j] = Math.max((i + 1 == h ? 0 : dp[i + 1][h - 1]) + 2, dp[i][j]);
                        if (max < dp[i][j]) {
                            max = dp[i][j];
                            left = i;
                        }
                    }
                }
            }
        }
        return s.substring(left,left+max);
    }
    //leetcode标准答案
    public String longestPalindrome3(String s) {
        int n = s.length();
        String res = null;
        int start = 0, maxLen = 0;
        boolean[][] dp = new boolean[n][n];
        for(int i = n - 1; i >= 0; i--) {
            for(int j = i; j < n; j++) {
                dp[i][j] = (s.charAt(i) == s.charAt(j)) && (j-i < 3 || dp[i+1][j-1]);
                if(dp[i][j] && (j-i+1 > maxLen)) {
                    start = i;
                    maxLen = j-i+1;
                }
            }
        }
        return s.substring(start, start + maxLen);
    }



    public static void main(String[] args) {
        longestPalindrome1("abcaa");
    }
}
