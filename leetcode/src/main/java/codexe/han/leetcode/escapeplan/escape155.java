package codexe.han.leetcode.escapeplan;

import java.util.LinkedList;

/**
 * stack中每个位置都保存数值与当前最小值的差值，小于负数的时候，说明是最小值交界处，需要更换最小值
 * stack.push的时候永远是push(x-min)
 */
public class escape155 {
    class MinStack {
        private long min;
        private LinkedList<Long> stack;
        public MinStack() {
            this.stack = new LinkedList<Long>();
        }

        public void push(int x) {
            if(stack.isEmpty()) {
                stack.push(0l);
                min = x;
            }
            else {
                if(x<min) {
                    stack.push(x-min);//<0
                    min = x;
                }
                else {
                    stack.push(x-min);
                }
            }
        }

        public void pop() {
            long peek = stack.pop();
            if(peek<0) {
                //change min
                min = min-peek;
            }
        }

        public int top() {

            long peek = stack.peek();
            if(peek>0) {
                //change min
                return (int)(min+peek);
            }
            else {
                return (int)min;
            }

        }

        public int getMin() {
            return (int)min;
        }
    }

}

/**
 Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.

 push(x) -- Push element x onto stack.
 pop() -- Removes the element on top of the stack.
 top() -- Get the top element.
 getMin() -- Retrieve the minimum element in the stack.


 Example:

 MinStack minStack = new MinStack();
 minStack.push(-2);
 minStack.push(0);
 minStack.push(-3);
 minStack.getMin();   --> Returns -3.
 minStack.pop();
 minStack.top();      --> Returns 0.
 minStack.getMin();   --> Returns -2.
 */
