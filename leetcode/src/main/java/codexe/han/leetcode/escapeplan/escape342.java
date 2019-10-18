package codexe.han.leetcode.escapeplan;

public class escape342 {
    public boolean isPowerOfFour(int num) {
        //先判断是2的幂数
        if(num>0&&((num-1)&num)==0){
            //有偶数个0 num-1是三的倍数
            return (num-1)%3==0;
        }
        return false;
    }
}
