package codexe.han.leetcode.escapeplan;

/**
 * 1.桶排序，统计0，1，2出现的次数，然后分别插入
 * 遍历两次
 *
 * 2.0放到开头， 2放到结尾， 做swap, 1最后会被交换到中间位置
 * 遍历一次
 */
public class escape75 {

    public void sortColors(int[] nums){
        int i=0, left=0, right=nums.length-1;

        while(i<=right) {
            if (nums[i] == 0) {
                swap(i, left, nums);
                left++;
            } else if (nums[i] == 2) {
                swap(i, right, nums);
                right--;
            }
            i++;
        }

    }
    public void swap(int i, int j, int[] nums){
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
