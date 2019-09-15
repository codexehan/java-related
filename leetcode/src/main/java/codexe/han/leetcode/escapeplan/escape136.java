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
