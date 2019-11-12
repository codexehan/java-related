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

/**
 Write a function that takes an unsigned integer and return the number of '1' bits it has (also known as the Hamming weight).



 Example 1:

 Input: 00000000000000000000000000001011
 Output: 3
 Explanation: The input binary string 00000000000000000000000000001011 has a total of three '1' bits.
 */
