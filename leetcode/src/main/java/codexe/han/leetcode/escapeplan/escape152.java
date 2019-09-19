package codexe.han.leetcode.escapeplan;

/**
 * 动态规划
 *
 * 当前dp[i] = Math.max(dp[i-1]*nums[i], nums[i])  正数乘以正数
 * 也有可能是负数乘以辅助 所以还有保存最小值
 *
 * [-2,3,-4]
 */
public class escape152 {
    public static void main(String[] args) {
        maxProduct(new int[]{0,2});
    }
    public static int maxProduct(int[] nums) {
        if(nums.length==0) return 0;
        int max = Integer.MIN_VALUE;
        int[] dpmax = new int[nums.length];
        int[] dpmin = new int[nums.length];
        for(int i=0;i<nums.length;i++){
            if(i==0) {
                dpmax[0]=nums[i];
                dpmin[0]=nums[i];
                max =nums[i];
            }
            else{
                dpmax[i] = Math.max(nums[i],Math.max(dpmin[i-1]*nums[i],dpmax[i-1]*nums[i]));
                dpmin[i] = Math.min(nums[i],Math.min(dpmin[i-1]*nums[i],dpmax[i-1]*nums[i]));
                max = Math.max(max,dpmax[i]);
            }
        }
        return max;
    }
}
