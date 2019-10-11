package codexe.han.leetcode.escapeplan;

import java.util.*;

/**
 * 滑动窗口
 *
 */
public class escape438 {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int sLen = s.length(),  pLen = p.length(), j = 0;
        if(sLen == 0 || pLen == 0 || sLen < pLen)
        {
            return result;
        }

        int [] counts = new int[256];

        for(char c : p.toCharArray())
        {
            counts[c]++;
        }

        int counter = pLen;
        int begin = 0;
        int end = 0;
        char [] sc = s.toCharArray();

        while(end < sLen)
        {
            if(counts[sc[end++]]-- > 0)
            {
                counter--;
            }

            while(counter == 0)
            {
                if(counts[sc[begin]]++ == 0)
                {
                    counter++;
                }
                if((end - begin) == pLen)
                {
                    result.add(begin);
                }
                begin++;
            }
        }

        return result;
    }

    //https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92007/Sliding-Window-algorithm-template-to-solve-all-the-Leetcode-substring-search-problem.
    //s: "cbaebabacd" p: "abc"
    public List<Integer> slidingWindowTemplateByHarryChaoyangHe(String s, String t) {
        //init a collection or int value to save the result according the question.
        List<Integer> result = new LinkedList<>();
        if(t.length()> s.length()) return result;

        //create a hashmap to save the Characters of the target substring.
        //(K, V) = (Character, Frequence of the Characters)
        Map<Character, Integer> map = new HashMap<>();
        for(char c : t.toCharArray()){
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        //maintain a counter to check whether match the target string.
        int counter = map.size();//must be the map size, NOT the string size because the char may be duplicate.

        //Two Pointers: begin - left pointer of the window; end - right pointer of the window
        int begin = 0, end = 0;

        //the length of the substring which match the target string.
        int len = Integer.MAX_VALUE;

        //loop at the begining of the source string
        while(end < s.length()){
            //s: "cbaebabacd" p: "abc"
            char c = s.charAt(end);//get a character

            if( map.containsKey(c) ){
                map.put(c, map.get(c)-1);// plus or minus one
                if(map.get(c) == 0) counter--;//modify the counter according the requirement(different condition).
            }
            end++;

            //increase begin pointer to make it invalid/valid again
            while(counter == 0 /* counter condition. different question may have different condition */){

                char tempc = s.charAt(begin);//***be careful here: choose the char at begin pointer, NOT the end pointer
                if(map.containsKey(tempc)){
                    map.put(tempc, map.get(tempc) + 1);//plus or minus one
                    if(map.get(tempc) > 0) counter++;//modify the counter according the requirement(different condition).
                }

                /* save / update(min/max) the result if find a target*/
                // result collections or result int value

                begin++;
            }
        }
        return result;
    }
}
