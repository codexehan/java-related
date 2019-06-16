package codexe.han.leetcode.escapeplan;

/**
 * 动态规划
 *
 * 判断前面数字串最后一个数字 和当前数字是否可以结合成一个10-20之间的数字
 */
public class escape91 {
    public static void main(String[] args) {
        numDecodings("226");
    }
    public static int numDecodings(String s) {
        int currN = 1;
        int prevN = 1;
        for(int i=1; i<s.length(); i++){
            if(i>0){
                int a = (s.charAt(i-1)-'0')*10+s.charAt(i)-'0';//"101"这种情况需要特殊处理
                if(a<=26&&a>=10) {
                    currN = currN + prevN;
                    prevN = currN -prevN;
                }
            }
        }
        return currN;
    }
}
