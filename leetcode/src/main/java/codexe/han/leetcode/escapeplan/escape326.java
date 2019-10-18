package codexe.han.leetcode.escapeplan;

public class escape326 {
    public static void main(String[] args) {
        isPowerOfThree(27);
    }
    public static boolean isPowerOfThree(int n) {
        if(n==0) return false;

        while(n%3==0&&n!=1){
            n = n/3;
        }
        return n==1;
    }
}
