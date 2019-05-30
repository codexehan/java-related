package codexe.han.leetcode.escapeplan;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 根据'/'提取，但是要注意 末尾没有/的情况！！！！
 *
 * 还有一种方法是利用split进行
 */
//TODO:Test
public class escape71 {
    public static void main(String[] args) {
        simplifyPath("/aa/..");
    }
    public static String simplifyPath(String path) {
        LinkedList<String> stack = new LinkedList<>();
        int j=1;
        int i=1;
        for(;i<path.length();i++){
            if(path.charAt(i)=='/'){//   xxx/  或者 /sss
                String folder = path.substring(j,i);
                j=i+1;
                if(folder.isEmpty()||folder.equals(".")) continue;
                if(folder.equals("..")){
                    stack.pollLast();
                }
                else{
                    stack.addLast("/"+folder);
                }
            }
        }
        if(path.charAt(i-1)!='/'){//   xxx/  或者 /sss
            String folder = path.substring(j,i);
            j=i+1;
            if(folder.isEmpty()||folder.equals(".")){

            }
            else if(folder.equals("..")){
                stack.pollLast();
            }
            else{
                stack.addLast("/"+folder);
            }
        }
        StringBuilder res = new StringBuilder();
        for(String r : stack){
            res.append(r);
        }
        return res.length()==0?"/":res.toString();
    }
}
