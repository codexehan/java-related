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
}
