package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

public class escape673 {
    public static void main(String[] args) {
        findNumberOfLIS(new int[]{1,3,5,4,7});
    }
    public static int findNumberOfLIS(int[] nums) {
        int max = 1;
        int finalMax = 1;
        int[] dp = new int[nums.length];
        int[] count = new int[nums.length];
        Arrays.fill(count,1);
        for(int i=0;i<nums.length;i++){
            for(int j =i;j>=0;j--){
                if(nums[i]>nums[j]){
                    //这个循环只会找到最终最大值对应的结果，非最大值的结果最终会被覆盖
                    //这段是最关键的
                    if(dp[j]+1>max){
                        max = dp[j]+1;
                        count[i] = count[j];
                    }
                    else if(dp[j]+1==max){
                        count[i] +=count[j];
                    }
                    //ignore < max
                }
            }
            dp[i] = max;
            finalMax = Math.max(finalMax,max);
            max = 1;
        }
        int res = 0;
        for(int i=0;i<count.length;i++){
            if(dp[i]==finalMax){
                res +=count[i];
            }
        }
        return res;
    }
}
