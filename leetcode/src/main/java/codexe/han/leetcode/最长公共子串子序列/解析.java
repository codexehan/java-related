package codexe.han.leetcode.最长公共子串子序列;

public class 解析 {
    /**
     * 1.最长公共子序列
     *
     * 如果当前s[i] == p[j], 那么，s[i] p[j]一定是i,j之前最长公共子串的结尾字符。
     * 所以，如何统计i,j之前的最长公共子串？
     * 首先明确dp[][]二位数组代表的含义:表示当前ij下的最长公共子串的长度
     * 那么，
     * 1.s[i]==p[j], dp[i][j] = dp[i-1][j-1]+1
     * 2.s[i]!=p[j], dp[i][j] = Math.max(dp[i-1][j],dp[i][j-1])
     * 解释第二条
     * bcd i
     * abc j
     * dp[i-1][j] bc abc
     * dp[i][j-1] bcd ab
     *
     * 结果dp[m][n]就是最长公共子序列的长度
     *
     * for(int i=1;i<s1.length();i++)
     *    for(int j=1;j<s2.length();j++)
     * 保证从上到下，从左到右，计算i,j的时候 i-1,j i,j-1都已经被计算过
     * ---------------------------------------------------------------
     * 2.最长公共子串
     * 斜对角线最长的那一条就是最长公共子串
     * 1.s[i]==p[j], dp[i][j] = dp[i-1][j-1]+1
     * 2.s[i]!=p[j], dp[i][j] = 0
     *
     * 结果就是对角线最长的那一条，所以需要添加一个max数值来保存
     */
}
