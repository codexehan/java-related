package codexe.han.leetcode.escapeplan;

/**
 * 基本思路
 * 利用之前已经算好的结果计算
 * 如何 计算好x*x  下次一计算 就可以 直接计算（x*x）*(x*x)
 * n=10 ((x*x)*(x*x)*x)*((x*x)*(x*x)*x)
 * n=5 (x*x)*(x*x)*x
 * n=2 x*x
 * n=1 x
 *
 * n=11 ((x*x)*(x*x)*x)*((x*x)*(x*x)*x)*x
 * n=5 (x*x)*(x*x)*x
 * n=2 x*x
 * n=1 x
 */
public class escape50 {
    public static void main(String[] args) {
        System.out.println(myPow(2,10));;
    }
    public static double myPow(double x, int n) {
        System.out.println(n);
        if(x<0) {
            x= 1/x;
            n=-n;//注意n可能会溢出，当n=Integer.MIN_VALUE的时候，x>1,分母无穷大，return 0; x==1,结果还是1；
        }
        if(n==0) return 1;
        if(n==1) return x;
        double res = myPow(x, n/2);
       // if(res*res/res != res || res*res != res*res*x/x) return 0;//stack overflow
        return n%2==0? res*res:res*res*x;
    }
}
