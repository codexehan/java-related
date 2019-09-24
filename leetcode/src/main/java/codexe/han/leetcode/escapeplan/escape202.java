package codexe.han.leetcode.escapeplan;

import java.util.HashSet;
import java.util.Set;

/**
 * A happy number is a number defined by the following process:
 * Starting with any positive integer, replace the number by the sum of the squares of its digits,
 * and repeat the process until the number equals 1 (where it will stay),
 * or it loops endlessly in a cycle which does not include
 * 1. Those numbers for which this process ends in 1 are happy numbers.
 *
 * Input: 19
 * Output: true
 * Explanation:
 * 12 + 92 = 82
 * 82 + 22 = 68
 * 62 + 82 = 100
 * 12 + 02 + 02 = 1
 */
public class escape202 {
    public static void main(String[] args) {
        System.out.println(isHappy(145));
    }
    public static boolean isHappy(int n) {
        int tmp;
        Set<Integer> res = new HashSet<>();
        while(!res.contains(n)){
            res.add(n);
            tmp=0;
            if((n%10==0&&n!=0)) return true;//100不执行循环的结果
            while((n%10)!=0){//100直接不执行循环，tmp等于0  但是20会出问题，所以应该从高位开始取值
                tmp += Math.pow((n%10),2);
                n = n/10;
            }
            if(tmp==1) return true;
            n=tmp;
            System.out.println(n);
        }
        return false;
    }

}
