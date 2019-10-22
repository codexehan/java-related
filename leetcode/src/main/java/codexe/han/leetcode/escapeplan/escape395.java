package codexe.han.leetcode.escapeplan;

//每一个字符都重复出现k次
//真是件悲哀的事情
public class escape395 {
    public int longestSubstring(String s, int k) {
        if(s.length()==0) return 0;
        int res = 1;
        int max = 1;
        for(int high=1;high<s.length();high++){
            if(s.charAt(high)==s.charAt(high-1)){
                res++;
                max = Math.max(res,max);
            }
            else{
                res = 1;
            }
        }
        return max;
    }
}
