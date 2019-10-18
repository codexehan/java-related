package codexe.han.leetcode.escapeplan;

public class escape231 {
    public boolean isPowerOfTwo(int n) {
        //2*2*2....  10...00...
        return n>0&&((n&(n-1))==0);
    }
}
