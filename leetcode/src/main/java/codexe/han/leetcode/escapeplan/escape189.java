package codexe.han.leetcode.escapeplan;

/**
 * 把数组后k个数，放到数组的前k个位置
 *
 * k位置分为两部分
 * 两边各自做首尾对调
 * 然后整个做首尾对调
 */
public class escape189 {
    public void rotate(int[] nums, int k) {
        k=k%nums.length;//大于length 取模
        reverse(nums, 0, nums.length - k - 1);
        reverse(nums, nums.length - k, nums.length - 1);
        reverse(nums, 0, nums.length - 1);
    }
    public void reverse(int[] nums, int left, int right){
        while(left<right){
            int tmp = nums[left];
            nums[left++]=nums[right];
            nums[right--]=tmp;
        }
    }
}
