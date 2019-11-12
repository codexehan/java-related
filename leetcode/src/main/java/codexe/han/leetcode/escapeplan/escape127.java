package codexe.han.leetcode.escapeplan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO
 */
public class escape127 {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> pool = new HashSet<>(wordList);
        Set<String> beginWords = new HashSet<>();
        beginWords.add(beginWord);
        Set<String> endWords = new HashSet<>();
        endWords.add(endWord);
        return dfs(beginWords, endWords, pool);
    }
    public int dfs(Set<String>beginWords, Set<String>endWords, Set<String> pool){
        if(beginWords.isEmpty()||endWords.isEmpty()) return 0;
        pool.removeAll(beginWords);
        Set<String> newBeginWords = new HashSet<>();
        for(String beginWord : beginWords){
            char[] chars = beginWord.toCharArray();
            for(int i=0; i<chars.length; i++){
                char prev = chars[i];
                for(char ch ='a';ch<='z';ch++){
                    if(ch == prev) continue;//跳过包含在beginWords中的内容
                    chars[i] = ch;
                    if(pool.contains(chars.toString())){
                        if(endWords.contains(chars.toString())){
                            return 2;
                        }
                        newBeginWords.add(chars.toString());
                    }
                }
                chars[i] = prev;
            }
        }
        int res;
        if (newBeginWords.size() <= endWords.size()) {
            res = dfs(newBeginWords, endWords, pool)+1;
        } else {
            res = dfs(endWords, newBeginWords, pool)+1;
        }
        return res==0?0:res+1;
    }
}

/**
 Given two words (beginWord and endWord), and a dictionary's word list, find the length of shortest transformation sequence from beginWord to endWord, such that:

 Only one letter can be changed at a time.
 Each transformed word must exist in the word list. Note that beginWord is not a transformed word.
 Note:

 Return 0 if there is no such transformation sequence.
 All words have the same length.
 All words contain only lowercase alphabetic characters.
 You may assume no duplicates in the word list.
 You may assume beginWord and endWord are non-empty and are not the same.
 Example 1:

 Input:
 beginWord = "hit",
 endWord = "cog",
 wordList = ["hot","dot","dog","lot","log","cog"]

 Output: 5

 Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
 return its length 5.
 */