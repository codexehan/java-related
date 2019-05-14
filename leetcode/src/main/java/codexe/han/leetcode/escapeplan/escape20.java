package codexe.han.leetcode.escapeplan;

import java.util.LinkedList;

/**
 * 遇见左括号，可以入栈
 * 遇见右括号，一定是已经出错了！！！！
 *
 * LinkedList做stack
 * push是在第一个元素之前添加，不是在list tail, 而是在list head之前
 */
public class escape20 {
    public static void main(String[] args) {
        String s = "([]){}";
        isValid(s);
    }
    public static boolean isValid(String s){
        LinkedList<Character> stack = new LinkedList<>();
        for(char ch : s.toCharArray()){
            if(stack.isEmpty()||stack.peekFirst()=='(' || stack.peekFirst()=='['|| stack.peekFirst()=='{'){
                if(ch == '(' || ch == '[' || ch == '{'){
                    stack.push(ch);
                }
                else{
                    if(stack.isEmpty()) return false;
                    char stackTop = stack.pollFirst();//假设栈最后只有一个元素，不能直接出栈
                    if (!((stackTop == '(' && ch == ')') || (stackTop == '[' && ch == ']') || (stackTop == '{' && ch == '}'))) {
                        return false;
                    }
                }
            }
            else{
                return false;
            }
        }
        return stack.isEmpty();
    }
}
