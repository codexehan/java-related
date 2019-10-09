package codexe.han.leetcode.escapeplan;

/**
 * Input:  [1,2,3,4]
 * Output: [24,12,8,6]
 *
 * input:[a,b,c,d]
 * output:[bcd,acd,abd,abc]
 * 分解output，连续的相乘放在一起
 * [1,a,ab,abc] -> 正向相乘
 * [bcd,cd,d,1] -> 反向相乘
 * 有点动态规划的感觉
 *
 */
public class escape238 {
    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];
        res[0] = 1;
        for(int i=1;i<nums.length;i++){
            res[i]=res[i-1]*nums[i-1];
        }
        int tmp =1;
        for(int i=nums.length-1;i>=0;i--){
            res[i]*=tmp;
            tmp *=nums[i];
        }
        return res;
    }
}
