package codexe.han.leetcode.escapeplan;


public class escape28 {
    public int strStr(String haystack, String needle){
        int i =0, j=0;
        for(; i<=haystack.length()-needle.length(); i++){
            /*for(j=0;j<needle.length();){
                if(haystack.charAt(j+i)==needle.charAt(j)){
                    j++;
                }
                else{
                    break;
                }
            }*/
            for(j=0;j<needle.length()&&haystack.charAt(j+i)==needle.charAt(j);j++);
            if(j==needle.length()) return i;
        }
        return -1;
    }
}
