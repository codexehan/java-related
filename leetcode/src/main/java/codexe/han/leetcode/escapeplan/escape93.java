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
    public static void main(String[] args) {
        restoreIpAddresses("1111");
    }
    public static List<String> restoreIpAddresses(String s) {
        List<String> finalRes = new ArrayList<>();
        StringBuilder res = new StringBuilder();
        backtracking(finalRes, res, s, 1,0);
        return finalRes;
    }
    public static void backtracking(List<String> finalRes, StringBuilder res, String s, int level, int start){

        if(level == 5 && start ==s.length()) {
            finalRes.add(res.toString());
        }

        if(level<=4) {
            if(start<s.length()) {
                res.append(s, start, start + 1);
                if(level!=4)res.append(".");
                backtracking(finalRes, res, s, level + 1, start + 1);
                if(level!=4) res.delete(res.length() - 2, res.length());
                else res.delete(res.length() - 1, res.length());
            }

            if(start<s.length()-1 && s.charAt(start)!='0') {
                res.append(s, start, start + 2);
                if(level!=4)res.append(".");
                backtracking(finalRes, res, s, level + 1, start + 2);
                if(level!=4) res.delete(res.length() - 3, res.length());
                else res.delete(res.length() - 2, res.length());
            }

            if (start<s.length()-2 && s.charAt(start)!='0' && Integer.valueOf(s.substring(start, start + 3)) <= 255) {
                res.append(s, start, start + 3);
                if(level!=4)res.append(".");
                backtracking(finalRes, res, s, level + 1, start + 3);
                if(level!=4) res.delete(res.length() - 4, res.length());
                else res.delete(res.length() - 3, res.length());
            }
        }
    }
}
