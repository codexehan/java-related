package codexe.han.leetcode.escapeplan;

import java.util.List;

/**
 * Input: s = "leetcode", wordDict = ["leet", "code"]
 * Output: true
 *
 * Input: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * Output: false
 *
 * 不能算是严格意义的动态规划，总之就是添加一个dp数组，记录连续的true，就说明一直到最后都会有true
 */
public class escape139 {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length()+1];
        dp[0]=true;
        for(int i=1;i<=s.length();i++){
            for(int j=i-1;j>=0;j--){
                dp[i] = dp[j]&&wordDict.contains(s.substring(j,i));//只有当和[最近的]一个word（dp[j]=true）&& 又包含的时候
                if(dp[i]) break;
            }
        }
        return dp[dp.length-1];
    }
}
