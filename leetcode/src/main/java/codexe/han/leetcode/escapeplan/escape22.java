package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

/**
 * 回溯算法
 * 剪枝条件
 * 【右括号的数目一定小于左括号】才可以放右括号
 * 【左括号数目必须小于】才可以放左括号
 */
public class escape22 {
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        backtracking(res, "", 0, 0, n);
        return res;
    }

    public void backtracking(List<String> res, String str, int left, int right, int n){
        if(left == right && left == n){
            res.add(str);
        }
        //if(left < right) return;

        if(left > right){
            //只有right<left的时候，才可以放右括号
            backtracking(res, str+")", left, right+1, n);
        }

        if(left < n){
            //有可能left数目超过了应有的范围，就不需要再做进一步处理了,left<n也包含了left>right的情况
            backtracking(res, str+"(", left+1, right, n);
        }
    }
}
