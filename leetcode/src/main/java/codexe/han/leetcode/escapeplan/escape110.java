package codexe.han.leetcode.escapeplan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class escape110 {
    boolean isBalanced = true;
    public boolean isBalanced(TreeNode root) {
        check(root);
        return isBalanced;
    }
    public int check(TreeNode node){
        if(node == null) return 0;
        int heightL = check(node.left);
        int heightR = check(node.right);
        if(isBalanced) isBalanced = Math.abs(heightL-heightR)<=1;
        return Math.max(heightL, heightR)+1;
    }
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
}


class Solution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> pool = new HashSet<>(wordList);
        if (!pool.contains(endWord))
            return 0;

        Set<String> beginWords = new HashSet<>();
        Set<String> endWords = new HashSet<>();
        beginWords.add(beginWord);
        endWords.add(endWord);

        return bfs(beginWords, endWords, pool);
    }

    private int bfs(Set<String> beginWords, Set<String> endWords, Set<String> pool) {
        if (beginWords.size() == 0)
            return 0;

        pool.removeAll(beginWords);
        Set<String> temp = new HashSet<>();

        for (String s : beginWords) {
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char pre = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == pre)
                        continue;

                    chars[i] = c;
                    String temp_str = new String(chars);
                    if (pool.contains(temp_str)) {
                        if (endWords.contains(temp_str))
                            return 2;

                        temp.add(temp_str);
                    }
                }
                chars[i] = pre;
            }
        }

        int res;
        if (temp.size() <= endWords.size()) {
            res = bfs(temp, endWords, pool);
        } else {
            res = bfs(endWords, temp, pool);
        }

        return res == 0 ? 0 : res + 1;
    }
}
/**
 Given a binary tree, determine if it is height-balanced.

 For this problem, a height-balanced binary tree is defined as:

 a binary tree in which the left and right subtrees of every node differ in height by no more than 1.



 Example 1:

 Given the following tree [3,9,20,null,null,15,7]:

 3
 / \
 9  20
 /  \
 15   7
 Return true.

 Example 2:

 Given the following tree [1,2,2,3,3,null,null,4,4]:

 */