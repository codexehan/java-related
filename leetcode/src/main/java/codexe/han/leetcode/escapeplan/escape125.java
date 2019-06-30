package codexe.han.leetcode.escapeplan;

/**
 * tow pointers
 * 数字 或者 字母
 */
public class escape125 {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length()-1;
        s=s.toLowerCase();
        while(left<right){
            while(left<right && !isNumbericAlpha(s.charAt(left))) left++;
            while(left<right && !isNumbericAlpha(s.charAt(right))) right--;
            if(left<right && s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
    public boolean isNumbericAlpha(char ch){
        return (ch >= 'a' && ch<='z') || (ch >= '0' && ch <= '9');
    }
}
