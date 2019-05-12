package codexe.han.leetcode.escapeplan;

/**
 * 考虑溢出的情况
 */
public class escape7 {
    public int reverse(int x) {
        int res=0,tmp = 0;
        while(x!=0){
            tmp = x%10 + 10*res;
            if((tmp-x%10)/10 != res) return 0;//因为10*res可能会溢出，溢出以后，正常加减、 所以需要在等式左边除以10来判断是否溢出
            x = x/10;
            res = tmp;
        }
        return res;
    }
}
