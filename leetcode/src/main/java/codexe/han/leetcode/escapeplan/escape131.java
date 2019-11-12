package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

/**
 * 回溯算法
 * 遍历每个子序列，
 * abcba
 * a
 * ab
 * abc
 * abcd
 * abcba
 */
public class escape131 {
    public List<List<String>> partition(String s) {
        List<List<String>> finalRes = new ArrayList<>();
        List<String> res = new ArrayList<>();
        backtracking(s,finalRes, res, 0);
        return finalRes;
    }

    public void backtracking(String s, List<List<String>> finalRes, List<String>res, int start){
        if(start==s.length()) {
            finalRes.add(new ArrayList<>(res));
            return;
        }
        for(int i=start; i<s.length(); i++){
            if(isPalindrom(s,start,i)){
                res.add(s.substring(start, i+1));
                backtracking(s,finalRes,res,i+1);
                res.remove(res.size()-1);
            }
        }
    }

    public boolean isPalindrom(String s, int start, int end){
        while(start<end){
            if(s.charAt(start++)!=s.charAt(end--)) return false;
        }
        return true;
    }
}

/**
 Given a string s, partition s such that every substring of the partition is a palindrome.

 Return all possible palindrome partitioning of s.

 Example:

 Input: "aab"
 Output:
 [
 ["aa","b"],
 ["a","a","b"]
 ]
 */
