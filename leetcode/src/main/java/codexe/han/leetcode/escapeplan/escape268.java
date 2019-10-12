package codexe.han.leetcode.escapeplan;

public class escape268 {
    public int missingNumber(int[] nums) {
        int n = nums.length;
        int res = 0;
        for(int i = 0;i<nums.length;i++){
            res +=nums[i];
        }
        return n*(n+1)/2-res;
    }
}
