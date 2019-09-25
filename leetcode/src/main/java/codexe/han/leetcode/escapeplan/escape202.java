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
        System.out.println(isHappy(12));
    }
    public static boolean isHappy(int n) {
        Set<Integer> set=new HashSet<Integer>();
        int tmp=n;
        int nn=0;
        while(!set.contains(tmp)){
            set.add(tmp);
            nn=tmp;
            tmp=0;
            while(nn!=0){
                tmp+=Math.pow(nn%10,2);
                nn=nn/10;
            }
            if(tmp==1) return true;
        }
        return false;
    }

}
