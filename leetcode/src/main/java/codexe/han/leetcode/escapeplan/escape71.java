package codexe.han.leetcode.escapeplan;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 根据'/'提取
 */
//TODO:Test
public class escape71 {
    public static void main(String[] args) {
        String p = "//home////ss/";
        for(String s : p.split("/")){

            System.out.println(s + ","+(s==""));
        }
    }
    public String simplifyPath(String path) {
        LinkedList<String> stack = new LinkedList<>();
        int j=0;
        for(int i=0;i<path.length();i++){
            if(path.charAt(i)=='/'){
                String folder = path.substring(j,i);
                if(folder.isEmpty()||folder.equals(".")) continue;
                if(folder.equals("..")){
                    stack.pollLast();
                }
                else{
                    stack.addLast("/"+folder);
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for(String r : stack){
            res.append(r);
        }
        return res.toString();
    }
}
