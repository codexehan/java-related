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
/**
 Given an array containing n distinct numbers taken from 0, 1, 2, ..., n, find the one that is missing from the array.

 Example 1:

 Input: [3,0,1]
 Output: 2
 Example 2:

 Input: [9,6,4,2,3,5,7,0,1]
 Output: 8
 */