package codexe.han.leetcode.回文问题;

/**
 Input: "abc"
 Output: 3
 Explanation: Three palindromic strings: "a", "b", "c".


 Example 2:

 Input: "aaa"
 Output: 6
 Explanation: Six palindromic strings: "a", "a", "a", "aa", "aa", "aaa".
 */
//统计dp[i][j]不为0的个数即可
public class leetcode647_PalindromicSubstrings {
    public static void main(String[] args) {
        countSubstrings("abc");
    }
    public static int countSubstrings(String s) {
        boolean[][] dp = new boolean[s.length()][s.length()];
        int count = 0;
        for(int j=0;j<s.length();j++){
            for(int i=j;i>=0;i--){
                if(s.charAt(i)==s.charAt(j)){
                    if(i==j||i==j-1){
                        dp[i][j] = true;
                    }
                    else{
                        dp[i][j]=dp[i+1][j-1];
                    }
                }
                else{
                    dp[i][j] = false;
                }
               if(dp[i][j]) count++;
            }
        }
        return count;
    }
}
