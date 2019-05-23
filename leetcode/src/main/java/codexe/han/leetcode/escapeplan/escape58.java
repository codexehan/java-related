package codexe.han.leetcode.escapeplan;

/**
 * 注意"a   "
 */
public class escape58 {
    public int lengthOfLastWord(String s) {
        int i=0;
        int j = s.length()-1;
        while(j>=0 && s.charAt(j)==' ') j--;
        for(i = j; i>=0 && s.charAt(i)!=' '; i--);
        return j-i;
    }
}
