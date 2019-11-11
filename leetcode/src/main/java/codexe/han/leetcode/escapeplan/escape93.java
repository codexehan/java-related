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

    ////////////////////直接使用循环////////////////////////
    public List<String> restoreIpAddresses1(String s) {
        List<String> res = new ArrayList<String>();
        int len = s.length();
        for (int i = 1; i < 4 && i < len - 2; i++) {//第一位尝试取第 1 2 3个数
            for (int j = i + 1; j < i + 4 && j < len - 1; j++) {
                for (int k = j + 1; k < j + 4 && k < len; k++) {
                    String s1 = s.substring(0, i),
                            s2 = s.substring(i, j),
                            s3 = s.substring(j, k),
                            s4 = s.substring(k, len);
                    if (isValid(s1) && isValid(s2)
                            && isValid(s3) && isValid(s4)) {
                        res.add(s1 + "." + s2 + "."
                                + s3 + "." + s4);
                    }
                }
            }
        }
        return res;
    }

    public boolean isValid(String s) {
        if (s.length() > 3 || s.length() == 0
                || (s.charAt(0) == '0' && s.length() > 1)
                || Integer.parseInt(s) > 255)
            return false;
        return true;
    }


}
/**
 Given a string containing only digits, restore it by returning all possible valid IP address combinations.

 Example:

 Input: "25525511135"
 Output: ["255.255.11.135", "255.255.111.35"]
 */
