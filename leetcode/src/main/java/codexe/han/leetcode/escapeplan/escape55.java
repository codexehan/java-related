package codexe.han.leetcode.escapeplan;

//TODO: TEST
public class escape55 {
    public boolean canJump(int[] nums){
        if(nums.length ==0) return true;
        int maxPos = nums[0];
        for(int i=0; i< nums.length && i<=maxPos; i++){
           maxPos = Math.max(nums[i] + i, maxPos);
        }
        return maxPos >= nums.length-1;
    }
}
