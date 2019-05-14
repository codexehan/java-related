package codexe.han.leetcode.escapeplan;

import java.util.LinkedList;

/**
 * 遇见左括号，可以入栈
 * 遇见右括号，一定是已经出错了！！！！
 */
public class escape20 {
    //TODO: Test
    public boolean isValid(String s){
        LinkedList<Character> stack = new LinkedList<>();
        for(char ch : s.toCharArray()){
            if(stack.peekLast()=='(' || stack.peekLast()=='['|| stack.peekLast()=='{'){
                if(ch == '(' || ch == '[' || ch == '{'){
                    stack.push(ch);
                }
                else{
                    char stackTop = stack.pollLast();
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
