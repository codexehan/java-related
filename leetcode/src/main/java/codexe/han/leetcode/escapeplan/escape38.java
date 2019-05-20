package codexe.han.leetcode.escapeplan;

public class escape38 {
    public static void main(String[] args) {
        System.out.println(countAndSay(5));
    }
    public static String countAndSay(int n) {
        StringBuilder res = new StringBuilder();
        if(n>=1){
            res.append('1');
        }


        while(--n>0){
            int count = 0;
            StringBuilder tmp = new StringBuilder();
            for(int i =0; i< res.length(); i++, count++){
                if(i!=0&&res.charAt(i-1)!=res.charAt(i)){//如果一直相等的话，i++, count++; 不相等的时候，需要生成结果
                    tmp.append(count);
                    tmp.append(res.charAt(i-1));
                    count = 0;
                }
            }
            if(count!=0){//末尾连续统计，需要计入
                tmp.append(count);
                tmp.append(res.charAt(res.length()-1));
            }
            res = tmp;
        }

        return res.toString();
    }
}
