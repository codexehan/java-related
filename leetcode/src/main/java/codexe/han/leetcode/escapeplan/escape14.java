package codexe.han.leetcode.escapeplan;

public class escape14 {
    public String longestCommonPrefix(String[] strs) {
        StringBuilder res = new StringBuilder();
        int minLength = Integer.MAX_VALUE;
        int n = strs.length;
        if(n!=0){
            for(String str : strs){
                minLength = Math.min(str.length(), minLength);
            }
            for(int i=0;i<minLength;i++){
                int j = 1;
                char ch = strs[0].charAt(i);
                while(j<n&&strs[j].charAt(i)== ch){
                    j++;
                }
                if(j==n){
                    res.append(ch);
                }
                else{
                    break;
                }
            }
        }
        return res.length()==0?"":res.toString();
    }
}
