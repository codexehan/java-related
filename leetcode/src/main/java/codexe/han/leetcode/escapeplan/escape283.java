package codexe.han.leetcode.escapeplan;

/**
 * 将所有0移动到末尾
 * 基本思路就是 two pointers 从头遍历数组，将非0的数字移动到数组前面，最后
 */
public class escape283 {
    public void moveZeroes(int[] nums) {
        int low = 0;
        for(int i=0;i<nums.length;i++){
            if(nums[i]!=0){
                nums[low++] = nums[i];
            }
        }
        for(;low<nums.length;low++){
            nums[low] = 0;
        }
    }
}
