package codexe.han.leetcode.escapeplan;

/**
 * 异或运算
 * 两数相同 结果为0 两数字不同，结果为1
 */
public class escape136 {
    public int singleNumber(int[] nums) {
        int n = nums[0];
        for(int i=1;i< nums.length;i++){
            n ^=nums[i];
        }
        return n;
    }
}

/**
 Given a non-empty array of integers, every element appears twice except for one. Find that single one.

 Note:

 Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?

 Example 1:

 Input: [2,2,1]
 Output: 1
 Example 2:

 Input: [4,1,2,1,2]
 Output: 4
 */
