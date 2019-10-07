package codexe.han.leetcode.escapeplan;

import java.util.LinkedList;

/**
 * + 号存整数，- 号存负数
 * * 号相乘，/ 号相除
 * 最后全部相加
 */
public class escape227 {
    public static void main(String[] args) {
        calculate(" 3/2 ");
    }
    public static int calculate(String s) {
        if(s.length()==0) return 1;
        LinkedList<Integer> stack = new LinkedList<>();
        char prevFlag = '+';
        int sum = 0;
        int prevRes = 1;
        s = s+'&';//为了避免"3+2" 没有被计算的情况
        for(int i=0;i<s.length();i++){
            char ch = s.charAt(i);
            if(ch==' ') continue;
            if(Character.isDigit(ch)){
                prevRes = prevRes*10+(ch-'0');//"423"是一个数字 所以这边要格外注意 res的值计算
            }
            else{
                //遇到运算符号，计算上一个运算符号的结果
                switch(prevFlag){
                    case '+':
                        stack.push(prevRes);
                        break;
                    case '-':
                        stack.push(-prevRes);
                        break;
                    case '*':
                        stack.push(prevRes*stack.pop());
                        break;
                    case '/':
                        stack.push(stack.pop()/prevRes);
                        break;
                }
                prevFlag = ch;//指向当前运算符
                prevRes = 1;
            }
        }
       // stack.push(prevRes);//最后一个prevRes还没有加入到stack中
        while(!stack.isEmpty()){
            sum += stack.pop();
        }
        return sum;
    }
}
