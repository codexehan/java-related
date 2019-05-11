package codexe.han.leetcode.escapeplan;

/**
 * Example 1:
 *
 * Input: "babad"
 * Output: "bab"
 * Note: "aba" is also a valid answer.
 *
 */
public class escape5 {

    private int maxLength;
    private int left;
    public String longestPalindrome(String s) {
        if(s.length() < 2){
            return s;
        }
        for(int i=0; i<s.length()-1;i++){
            getLength(s,i,i);
            getLength(s,i,i+1);
        }
        return s.substring(left, left+maxLength);
    }
    public void getLength(String s, int l, int r){
        while(l>=0 && r<s.length() && s.charAt(l)==s.charAt(r)){
            l--;
            r++;
        }
        if(maxLength < r-l-1){
            maxLength = r-l-1;
            left = l+1;
        }
    }
}
