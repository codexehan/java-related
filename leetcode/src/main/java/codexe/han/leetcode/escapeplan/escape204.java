package codexe.han.leetcode.escapeplan;

/**
 * 统计小于n的所有质数
 *
 * 反向思维 任何非质数都可以由被某个质数整除
 *
 * 如何记录某些数是不是素数 boolean数组
 */
public class escape204 {
    public int countPrimes(int n) {
        boolean[] notPrimeFlag = new boolean[n+1];//如果用来标记谁是prime的话，会有一个问题，因为我们只能靠着乘法知道谁不是，剩下的都是。所以应该标记谁不是prime
        //要么就标记谁是prime，然后再遍历一遍，统计res
      //  notPrimeFlag[2] = true;//n<2会造成outofbounding
        int res = 0;
        for(int i=2; i<n; i++){
            if(!notPrimeFlag[i]){
                res++;
                for(int j=2;i*j<=n;j++){
                    notPrimeFlag[i*j] = true;
                }
            }
        }
        return res;
    }
}
/**
 Count the number of prime numbers less than a non-negative number, n.

 Example:

 Input: 10
 Output: 4
 Explanation: There are 4 prime numbers less than 10, they are 2, 3, 5, 7.
 */