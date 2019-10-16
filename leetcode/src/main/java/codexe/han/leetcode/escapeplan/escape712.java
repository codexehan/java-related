package codexe.han.leetcode.escapeplan;

//按照最小公共子串的思想，dp每次存储的都是删除相关字符串后最大的ascii的值
//TODO:尚未测试
public class escape712 {
    public int minimumDeleteSum(String s1, String s2) {
        int[][] dp = new int[s1.length()+1][s2.length()+1];
        dp[0][0] = 0;
        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                if(s1.charAt(i)==s2.charAt(j)){
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
