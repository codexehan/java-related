package codexe.han.leetcode.escapeplan;

/**
 * 两个字符串字母一样
 */
public class escape242 {
    public boolean isAnagram(String s, String t) {
        int[] map = new int[26];
        if(s.length()!=t.length()) return false;//有了这一步，就可以再下面直接返回true
        for(char ch : s.toCharArray()){
            map[ch-'a']++;
        }
        for(char ch : t.toCharArray()){
            if(map[ch-'a']==0) return false;
            else {
                map[ch-'a']--;
            }
        }
        return true;
    }
}

/**
 Given two strings s and t , write a function to determine if t is an anagram of s.

 Example 1:

 Input: s = "anagram", t = "nagaram"
 Output: true
 Example 2:

 Input: s = "rat", t = "car"
 Output: false
 Note:
 You may assume the string contains only lowercase alphabets.
 */
