package codexe.han.leetcode.escapeplan;

/**
 * remove duplicates
 */
public class escape26 {
    public int removeDuplicates(int[] nums) {
        int n =0;
        for(int i=0; i<nums.length; i++){
            if(i!=0 && nums[i-1]==nums[i]) continue;
            nums[n] = nums[i];
            n++;
        }
        return n;
    }

}
