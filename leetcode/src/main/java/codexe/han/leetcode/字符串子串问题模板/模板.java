package codexe.han.leetcode.字符串子串问题模板;

import java.util.*;

/**
 https://leetcode.com/problems/find-all-anagrams-in-a-string/discuss/92007/sliding-window-algorithm-template-to-solve-all-the-leetcode-substring-search-problem
 解决问题
 https://leetcode.com/problems/minimum-window-substring/
 https://leetcode.com/problems/longest-substring-without-repeating-characters/
 https://leetcode.com/problems/substring-with-concatenation-of-all-words/
 https://leetcode.com/problems/longest-substring-with-at-most-two-distinct-characters/
 https://leetcode.com/problems/find-all-anagrams-in-a-string/
 https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
 */
public class 模板 {
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
//不重复子串
class leetcode3_LongestSubstringWithoutRepeatingCharacters{
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();//存储当前子串中各字符出现的次数
        int counter=0;//重复出现的字符的个数 实际上就是map中value大于1的个数
        int begin=0, end=0;
        int res = 0;
        while(end<s.length()){
            Character chEnd = s.charAt(end);
            map.put(chEnd,map.getOrDefault(chEnd,0)+1);
            if(map.get(chEnd) >1){
                counter++;
            }

            while(counter!=0){
                //最长不重复子串 end固定，从begin开始找到的第一个就是
                Character chBegin = s.charAt(begin);
                if(map.get(chBegin)-1>0) counter--;
                map.put(chBegin,map.get(chBegin)-1);
                begin++;
            }
            end++;
            res = Math.max(res,end-begin);
        }
        return res;

/*

        Map<Character, Integer> map = new HashMap<>();
        int begin = 0, end = 0, counter = 0, d = 0;//counter用来统计多少当前字符串substring中，有多少个重复的字符

        while (end < s.length()) {
            // > 0 means repeating character
            //if(map[s.charAt(end++)]-- > 0) counter++;
            char c = s.charAt(end);
            map.put(c, map.getOrDefault(c, 0) + 1);
            if(map.get(c) > 1) counter++;
            end++;

            //从begin开始，找出第一个end之前所有字符串中counter是零的数字，那么一定是到当前end位置，最长的没有重复字符串的子串，记录下来
            while (counter > 0) {
                //if (map[s.charAt(begin++)]-- > 1) counter--;
                char charTemp = s.charAt(begin);
                if (map.get(charTemp) > 1) counter--;
                map.put(charTemp, map.get(charTemp)-1);
                begin++;
            }
            d = Math.max(d, end - begin);
        }
        return d;
        */
    }
}
//s = "anagram", t = "nagaram"
class leetcode242_ValidAnagram{
    public boolean isAnagram(String s, String t) {
        if(s.length()!=t.length()) return false;
        int[] count = new int[26];
        for(char ch : s.toCharArray()){
            count[ch-'a']++;
        }
        for(char ch : t.toCharArray()){
            if(count[ch-'a']--==0) return false;
        }
        return true;
    }
}

/**
 Input:
 s: "cbaebabacd" p: "abc"

 Output:
 [0, 6]

 Explanation:
 The substring with start index = 0 is "cba", which is an anagram of "abc".
 The substring with start index = 6 is "bac", which is an anagram of "abc".
 */
class leetcode438_FindAllAnagramsInAString {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        Map<Character, Integer> map  = new HashMap<>();
        for(char ch : p.toCharArray()){
            map.put(ch, map.getOrDefault(ch,0)+1);
        }
        int counter = map.size();//anagrams中应该包含的非重复字符个数

        int begin = 0, end = 0;
        while(end<s.length()){
            char chEnd = s.charAt(end);
            if(map.containsKey(chEnd)){
                if(map.get(chEnd)-1==0) counter--;//chEnd的个数变为0，说明已经有相应个数的chEnd出现 counter最小是0
                map.put(chEnd,map.get(chEnd)-1);
            }
            end++;

            while(counter==0){
                char chBegin = s.charAt(begin);
                if(map.containsKey(chBegin)){
                    map.put(chBegin,map.get(chBegin)+1);
                    if(map.get(chBegin)>0)counter++;//这边不会重复执行的，因为counter在此之后 已经不为0了
                }
                if((end-begin)==p.length()){
                    res.add(begin);
                }
                begin++;
            }
        }
        return res;

/*

        List<Integer> res = new ArrayList<>();

        List<Integer> result = new LinkedList<>();
        if(t.length()> s.length()) return result;
        Map<Character, Integer> map = new HashMap<>();
        for(char c : t.toCharArray()){
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        int counter = map.size();

        int begin = 0, end = 0;
        int head = 0;
        int len = Integer.MAX_VALUE;


        while(end < s.length()){
            char c = s.charAt(end);
            if( map.containsKey(c) ){
                map.put(c, map.get(c)-1);
                if(map.get(c) == 0) counter--;
            }
            end++;

            while(counter == 0){
                char tempc = s.charAt(begin);
                if(map.containsKey(tempc)){
                    map.put(tempc, map.get(tempc) + 1);
                    if(map.get(tempc) > 0){
                        counter++;
                    }
                }
                if(end-begin == t.length()){
                    result.add(begin);
                }
                begin++;
            }

        }
        return result;*/
    }
}

/**
 "ababbc"
 2
 */
class leetcode395_LongestSubstringWithAtLeastKRepeatingCharacters{
    public static void main(String[] args) {
        longestSubstring("ababbc",2);
    }
    public static int longestSubstring(String s, int k) {
        Map<Character, Integer> map = new HashMap<>();//统计当前子串中各个字符出现的次数
        int counter = map.size();//表示当前子串中，大于k的unique的字符个数
        int begin =0,end=0;
        int res = 0;
        while(end<s.length()){
            char chEnd = s.charAt(end);
            map.put(chEnd,map.getOrDefault(chEnd,0)+1);
            if(map.get(chEnd)==k) counter++;
            end++;

            while(counter==map.size()){
                char chBegin = s.charAt(begin);
                map.put(chBegin, map.get(chBegin)-1);
                if(map.get(chBegin)<k) counter--;

                res = Math.max(res,end-begin);

                begin++;
            }
        }
        return res;


        char[] str = s.toCharArray();
        int[] counts = new int[26];
        int h, i, j, idx, max = 0, unique, noLessThanK;

        for (h = 1; h <= 26; h++) {
            Arrays.fill(counts, 0);
            i = 0;
            j = 0;
            unique = 0;
            noLessThanK = 0;
            while (j < str.length) {
                if (unique <= h) {
                    idx = str[j] - 'a';
                    if (counts[idx] == 0)
                        unique++;
                    counts[idx]++;
                    if (counts[idx] == k)
                        noLessThanK++;
                    j++;
                }
                else {
                    idx = str[i] - 'a';
                    if (counts[idx] == k)
                        noLessThanK--;
                    counts[idx]--;
                    if (counts[idx] == 0)
                        unique--;
                    i++;
                }
                if (unique == h && unique == noLessThanK)
                    max = Math.max(j - i, max);
            }
        }

        return max;
    }
}
