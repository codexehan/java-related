package codexe.han.leetcode.escapeplan;

//TODO: Test
public class escape28 {
    public int strStr(String haystack, String needle){
        int i =0, j=0;
        for(; i<haystack.length()-needle.length(); i++){
            for(;j<needle.length();){
                if(haystack.charAt(j+i)==needle.charAt(j)){
                    j++;
                }
            }
            if(j==needle.length()-1) return i;
        }
        return -1;
    }
}
