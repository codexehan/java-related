package codexe.han.leetcode.escapeplan;

/**
 * [-2,1,-3,4,-1,2,1,-5,4]
 * [4,-1,2,1]
 *
 这是一道非常经典的动态规划的题目，用到的思路我们在别的动态规划题目中也很常用，以后我们称为”局部最优和全局最优解法“。
 基本思路是这样的，在每一步，我们维护两个变量，一个是全局最优，就是到当前元素为止最优的解是，一个是局部最优，就是必须包含当前元素的最优的解。接下来说说动态规划的递推式（这是动态规划最重要的步骤，递归式出来了，基本上代码框架也就出来了）。假设我们已知第i步的global[i]（全局最优）和local[i]（局部最优），那么第i+1步的表达式是：
 local[i+1]=Math.max(A[i], local[i]+A[i])，就是局部最优是一定要包含当前元素，所以不然就是上一步的局部最优local[i]+当前元素A[i]（因为local[i]一定包含第i个元素，所以不违反条件），但是如果local[i]是负的，那么加上他就不如不需要的，所以不然就是直接用A[i]；
 global[i+1]=Math(local[i+1],global[i])，有了当前一步的局部最优，那么全局最优就是当前的局部最优或者还是原来的全局最优（所有情况都会被涵盖进来，因为最优的解如果不包含当前元素，那么前面会被维护在全局最优里面，如果包含当前元素，那么就是这个局部最优）。

 接下来我们分析一下复杂度，时间上只需要扫描一次数组，所以时间复杂度是O(n)。空间上我们可以看出表达式中只需要用到上一步local[i]和global[i]就可以得到下一步的结果，所以我们在实现中可以用一个变量来迭代这个结果，不需要是一个数组，也就是如程序中实现的那样，所以空间复杂度是两个变量（local和global），即O(2)=O(1)。

 最大sum的子序列必须是 以正数开头的
 一个整数前面只有两种情况，一是，前面sum大于等于0（保留） 二是，前面sum小于0（舍弃）
 *

 */
public class escape53 {
    public int maxSubArray(int[] nums){
        if(nums.length == 0) return Integer.MIN_VALUE;
        int local = nums[0];
        int global = local;
        for(int i =1; i<nums.length; i++){
            local = Math.max(nums[i], local+nums[i]);//local+n < n local是负数 ， 负数的情况下， 应该从当前数字重新开始计算最大连续子序列和
            global = Math.max(local, global);
        }
        return global;
    }
}
