package codexe.han.leetcode.escapeplan;

import java.util.*;

/**
 * 用最简单最直接的想法解答
 * 首先统计所有的字符出现次数，并记录下个数小于k的字符
 * 将字符串用上面的字符分成多个部分，分别校验每个子串中，出现的次数k
 */
//每一个字符都重复出现k次
//用字符串的通用模板
public class escape395 {
    public int longestSubstring(String s, int k){
        if(k<=0||s==null||s.length()==0) return 0;
        int max = 0;
        Set<Character> lessThanK = new HashSet<>();
        int[] charCounts = new int[26];
        for(char ch : s.toCharArray()){
            charCounts[ch-'a']++;
        }
        for(int i=0;i<charCounts.length;i++){
            if(charCounts[i]>0&&charCounts[i]<k){
                lessThanK.add((char)(i+'a'));
            }
        }
        if(lessThanK.isEmpty()) return s.length();
        int begin = 0, end=0;
        while(end<s.length()){
            if(lessThanK.contains(s.charAt(end))) {
                if(end-begin>=k) {
                    max = Math.max(max, longestSubstring(s.substring(begin, end), k));
                }
                begin = end+1;
            }
            end++;
            /*if(lessThanK.contains(s.charAt(end))){
                while(end-begin>=k) {
                    //test begin to end
                   // if(check(s.substring(begin, end), k)) max = Math.max(end-begin,max);//不能在这里检测，baaabca 3
                    begin++;
                }
                    begin = end + 1;
            }
            end++;*/
        }
        return max;
    }
    private boolean check(String s, int k){
        int[] count = new int[26];
        for(int i=0;i<s.length();i++){
            count[s.charAt(i)-'a']++;
        }
        for(int i=0;i<s.length();i++){
            if(count[s.charAt(i)-'a']<k) return false;
        }
        return true;
    }
    public int longestSubstring1(String s, int k) {
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
                //unique用来标记当前
                if (unique <= h) {
                    idx = str[j] - 'a';
                    if (counts[idx] == 0)
                        unique++;
                    counts[idx]++;
                    if (counts[idx] == k)
                        noLessThanK++;//>=k
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
                if (unique == h && unique == noLessThanK)//所有的都出现次数都大于等于k
                    max = Math.max(j - i, max);
            }
        }

        return max;
    }

    //In each step, just find the infrequent elements (show less than k times) as splits
    // since any of these infrequent elements couldn't be any part of the substring we want.
    public int longestSubstring2(String s, int k) {
        if (s == null || s.length() == 0) return 0;
        char[] chars = new char[26];
        // record the frequency of each character
        for (int i = 0; i < s.length(); i += 1) chars[s.charAt(i) - 'a'] += 1;
        boolean flag = true;
        for (int i = 0; i < chars.length; i += 1) {
            if (chars[i] < k && chars[i] > 0) flag = false;
        }
        // return the length of string if this string is a valid string
        if (flag == true) return s.length();
        int result = 0;
        int start = 0, cur = 0;
        // otherwise we use all the infrequent elements as splits
        while (cur < s.length()) {
            if (chars[s.charAt(cur) - 'a'] < k) {
                result = Math.max(result, longestSubstring2(s.substring(start, cur), k));
                start = cur + 1;
            }
            cur++;
        }
        result = Math.max(result, longestSubstring2(s.substring(start), k));
        return result;
    }

}
/**
 Find the length of the longest substring T of a given string (consists of lowercase letters only) such that every character in T appears no less than k times.

 Example 1:

 Input:
 s = "aaabb", k = 3

 Output:
 3

 The longest substring is "aaa", as 'a' is repeated 3 times.
 Example 2:

 Input:
 s = "ababbc", k = 2

 Output:
 5

 The longest substring is "ababb", as 'a' is repeated 2 times and 'b' is repeated 3 times.
 */