package codexe.han.leetcode.escapeplan;

public class escape344 {
    public void reverseString(char[] s) {
        int i=0, j=s.length-1;
        while(i<j){
            char tmp = s[i];
            s[i++] = s[j];
            s[j--] = tmp;
        }
    }
}
/**
 * 做对调，两个指针收尾互相交换
 */
