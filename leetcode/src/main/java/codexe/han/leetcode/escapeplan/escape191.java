package codexe.han.leetcode.escapeplan;

/**
 * << 左移一位，相当于乘以2
 * >> 右移一位，相当于除以2
 * >>> 无符号位右移，空位都以0补齐
 */
public class escape191 {
    public int hammingWeight(int n) {
        int num = 0;
        while(n!=0){
            num += n&1;
            n = n>>>1;
        }
        return num;
    }
}
