package codexe.han.leetcode.escapeplan;

/**
 * 丑数是数字因子只包含2，3，5三个数，所以，丑数一定是之前的一个丑数*2 *3 *5 得到的
 * 动态规划
 */
public class escape264 {
    public int nthUglyNumber(int n) {
        if(n==1) return 1;
        int[] index = new int[3];
        int[] ugly = new int[n];
        ugly[0] = 1;
        int min;
        int tmp0,tmp1,tmp2;
        for(int i=1;i<n;i++){
            tmp0=ugly[index[0]]*2;
            tmp1=ugly[index[1]]*3;
            tmp2=ugly[index[2]]*5;
            min = Math.min(tmp0,Math.min(tmp1,tmp2));
            if(tmp0==min) index[0]++;
            if(tmp1==min) index[1]++;
            if(tmp2==min) index[2]++;
            ugly[i] = min;
        }
        return ugly[n-2];
    }
}
