package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

/**
 * 出现个数超过n/2说明一定存在排序数组的n/2的位置一定是该数字
 */
public class escape169 {
    public int majorityElement(int[] nums) {
        Arrays.sort(nums);
        return nums[nums.length/2];
    }
}
