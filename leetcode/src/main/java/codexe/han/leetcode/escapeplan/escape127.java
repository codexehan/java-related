package codexe.han.leetcode.escapeplan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if(beginWords.isEmpty()) return 0;
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
