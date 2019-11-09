package codexe.han.leetcode.escapeplan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class escape3 {
    public int lengthOfLongestSubstring(String s) {
        Map<Character,Integer> charSet = new HashMap<>();
        int start = 0;
        int end = 0;
        int length = 0;
        for(;end<s.length();end++){
            char ch = s.charAt(end);
            if(charSet.keySet().contains(ch)&&charSet.get(ch)>=start){
                length = Math.max(end-start,length);
                start = charSet.get(ch)+1;
            }
            charSet.put(ch, end);
        }
        length = Math.max(end-start,length);
        return length;
    }

    public int lengthOfLongestSubstring2(String s) {
        Map<Character, Integer> map = new HashMap<>();//存储当前子串中各字符出现的次数
        int counter = 0;//重复出现的字符的个数 实际上就是map中value大于1的个数
        int begin = 0, end = 0;
        int res = 0;
        while (end < s.length()) {
            Character chEnd = s.charAt(end);
            map.put(chEnd, map.getOrDefault(chEnd, 0) + 1);
            if (map.get(chEnd) > 1) {
                counter++;
            }
            end++;

            while (counter != 0) {
                //最长不重复子串 end固定，从begin开始找到的第一个就是
                Character chBegin = s.charAt(begin);
                if (map.get(chBegin) - 1 > 0) counter--;
                map.put(chBegin, map.get(chBegin) - 1);
                begin++;
            }
            res = Math.max(res, end - begin);
        }
        return res;
    }

}
