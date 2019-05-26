package codexe.han.leetcode.escapeplan;

/**
 * 如果是double的话
 * 牛顿迭代法求解非线性方程组的解
 *
 * x^2 = n
 *
 * f(x) = x^2-n
 *
 * 求f(x)=0
 *
 * f(x)' = 2x
 *
 * 任取一个点做切线，(x0,f(x0)),切线为y = f(x0)'(x-x0) + f(x0), 切线与x轴焦点的横坐标为 x = x0 - f(x0)/f(x0)'
 * 重复可得 xn+1 = xn - f(xn)/f(xn)'
 *
 * 当xn+1 与 xn无线接近的时候，获得最终解
 *
 * 如果是整数，思路就是挨个整数试一下，O(n)，在此基础上进行优化，才用二分查找，O(logn)
 */
public class escape69 {
    public int mySqrt(int x) {
        if(x==0) return 0;
        int left = 1;//从1开始 因为0已经被检验过了，避免了mid最后变为0的时候，无法做除法的问题
        int right = x/2+1;
        while(left<=right){
            int mid = (left+right)/2;
            if(mid==x/mid){
                return mid;
            }
            /*if(mid*mid<x){//溢出，不能直接使用* 改用除法
                left = left+1;
            }*/
            if(mid<x/mid){
                left = mid+1;
            }
            else{
                right = mid-1;
            }
        }
        return left-1;

    }
}
