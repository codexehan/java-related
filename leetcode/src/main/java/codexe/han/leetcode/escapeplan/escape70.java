package codexe.han.leetcode.escapeplan;

/**
 * 动态规划
 *
 * n的路径 由n-1, n-2相加构成
 */
public class escape70 {
    public int climbStairs(int n) {
        int n1 = 2;
        int n2 = 1;
        if(n==1) return 1;
        if(n==2) return 2;
        for(int i =3; i<=n; i++){
            int tmp = n1;
            n1 = n1+n2;
            n2 = tmp;
        }
        return n1;
    }
}
