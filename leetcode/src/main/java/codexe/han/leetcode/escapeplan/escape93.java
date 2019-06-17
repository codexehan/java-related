package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.List;

/**
 * 回溯算法
 * 单元测试
 * 1.三位数字
 * 2.四位数字
 * 3.五位数字   测试两位结合 没有三位结合
 * 4.六位数字   测试两位结合，三位结合
 *
 * 255的大小判定
 * 000连续的0的存在情况？  不要计算0开头的情况
 */
public class escape93 {
    public List<String> restoreIpAddresses(String s) {
        List<String> finalRes = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        backtracking(finalRes, res, s, 1,0);
        return finalRes;
    }
    public void backtracking(List<String> finalRes, StringBuilder res, String s, int level, int start){

        if(level == 4 && start ==s.length()) finalRes.add(res.toString());

        if(level<4) {
            if(start<s.length()) {
                res.append(s, start, start + 1);
                backtracking(finalRes, res, s, level + 1, start + 1);
                res.delete(res.length() - 1, res.length() + 1);
            }

            if(start<s.length()-1 && s.charAt(start)!='0') {
                res.append(s, start, start + 2);
                backtracking(finalRes, res, s, level + 1, start + 2);
                res.delete(res.length() - 1, res.length() + 2);
            }

            if (start<s.length()-2 && s.charAt(start)!='0' && Integer.valueOf(s.substring(start, start + 3)) <= 255) {
                res.append(s, start, start + 3);
                backtracking(finalRes, res, s, level + 1, start + 3);
                res.delete(res.length() - 1, res.length() + 3);
            }
        }
    }
}
