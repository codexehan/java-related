package codexe.han.leetcode.escapeplan;

/**
 * 动态规划
 *
 * 判断前面数字串最后一个数字 和当前数字是否可以结合成一个10-20之间的数字
 * 但是要注意当前数字是0的情况
 */
public class escape91 {
    public static void main(String[] args) {
        numDecodings("226");
    }
    public static int numDecodings(String s) {
        int currN = 1;
        int prevN = 1;
        int n=0;
        for(int i=0; i<s.length(); i++){
            n=0;
            int a = +s.charAt(i)-'0';
            if(a>=1 && a<=9){//当前数字是1-9才可以继续下去，否则n=0
                n = currN;
            }
            if(i>0) {
                a = (s.charAt(i - 1) - '0') * 10 + a;//"101"这种情况需要特殊处理
                if (a <= 26 && a >= 10) {//当前数字 和上一个数字组合是10-26才可以加上之前的n  pervN
                    n = n + prevN;
                }
            }
            prevN = currN;
            currN = n;
        }
        return currN;
    }

    //动态规划解法
    public int numDecodings2(String s) {
        if (s.isEmpty() || s.charAt(0) == '0') return 0;
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        for (int i = 1; i < dp.length; ++i) {
            dp[i] = (s.charAt(i - 1) == '0') ? 0 : dp[i - 1];
            if (i > 1 && (s.charAt(i - 2) == '1' || (s.charAt(i - 2) == '2' && s.charAt(i - 1) <= '6'))) {
                dp[i] += dp[i - 2];
            }
        }
        return dp[s.length()];
    }
}
/**
 A message containing letters from A-Z is being encoded to numbers using the following mapping:

 'A' -> 1
 'B' -> 2
 ...
 'Z' -> 26
 Given a non-empty string containing only digits, determine the total number of ways to decode it.

 Example 1:

 Input: "12"
 Output: 2
 Explanation: It could be decoded as "AB" (1 2) or "L" (12).
 Example 2:

 Input: "226"
 Output: 3
 Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 */