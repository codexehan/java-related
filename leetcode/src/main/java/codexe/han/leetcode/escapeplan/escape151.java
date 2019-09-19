package codexe.han.leetcode.escapeplan;

public class escape151 {
    public String reverseWords(String s) {
        String[] strs = s.split(" ");
        StringBuffer res = new StringBuffer();
        for(int i = strs.length - 1; i >= 0; i--){
            if(i!= strs.length - 1 && !strs[i].equals("")){
                res.append(" ");
            }
            res.append(strs[i]);
        }
        return res.toString();
    }
}
