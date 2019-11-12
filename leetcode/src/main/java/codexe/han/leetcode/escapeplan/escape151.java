package codexe.han.leetcode.escapeplan;

public class escape151 {
    public String reverseWords(String s) {
        String[] strs = s.split(" ");
        StringBuffer res = new StringBuffer();
        for(int i = strs.length - 1; i >= 0; i--){
            if(i!= strs.length - 1 && !strs[i].equals("")){
                res.append(" ");
            }
            res.append(strs[i]);
        }
        return res.toString();
    }
}

/**
 Given an input string, reverse the string word by word.



 Example 1:

 Input: "the sky is blue"
 Output: "blue is sky the"
 Example 2:

 Input: "  hello world!  "
 Output: "world! hello"
 Explanation: Your reversed string should not contain leading or trailing spaces.
 Example 3:

 Input: "a good   example"
 Output: "example good a"
 Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
 */
