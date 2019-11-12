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
                //dp[j] 是以j-1的字符结尾是否被包含
                dp[i] = dp[j]&&wordDict.contains(s.substring(j,i));//只有当和[最近的]一个word（dp[j]=true）&& 又包含的时候
                if(dp[i]) break;
            }
        }
        return dp[dp.length-1];
    }
}

/**
 Given a non-empty string s and a dictionary wordDict containing a list of non-empty words, determine if s can be segmented into a space-separated sequence of one or more dictionary words.

 Note:

 The same word in the dictionary may be reused multiple times in the segmentation.
 You may assume the dictionary does not contain duplicate words.
 Example 1:

 Input: s = "leetcode", wordDict = ["leet", "code"]
 Output: true
 Explanation: Return true because "leetcode" can be segmented as "leet code".
 Example 2:

 Input: s = "applepenapple", wordDict = ["apple", "pen"]
 Output: true
 Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
 Note that you are allowed to reuse a dictionary word.
 */
