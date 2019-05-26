package codexe.han.leetcode.escapeplan;

public class escape67 {
    public static void main(String[] args) {
        addBinary("11","1");
    }
    public static String addBinary(String a, String b) {
        StringBuilder res = new StringBuilder();
        int n = Math.max(a.length(), b.length());
        int count=0;
        for(int i=0;i <n;i++){
            int cha = i<a.length()?a.charAt(a.length()-1-i)-'0':0;//同时从最后一个数开始相加
            int chb = i<b.length()?b.charAt(b.length()-1-i)-'0':0;
            int sum = cha+chb+count;
            if(sum>=2){
                count = 1;
                res.insert(0,sum-2);
            }
            else{
                count=0;
                res.insert(0,sum);
            }
        }
        if(count ==1) res.insert(0,1);
        return res.toString();
    }
}
