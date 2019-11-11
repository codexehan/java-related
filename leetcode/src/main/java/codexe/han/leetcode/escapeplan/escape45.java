package codexe.han.leetcode.escapeplan;

public class escape45 {
    public int jump(int[] nums) {
        int jump = 0;
        int maxPos = 0;
        int farthest = 0;
        if(nums.length==1) return 0;//nums[0]=0
        //每一次跳跃，都计算最远能够跳跃到哪里，每达到当前最远的位置，就+1并切换到下一个最远的位置
        for(int i=0;i<=maxPos;i++){
            farthest = Math.max(farthest,nums[i]+i);
            if(i==maxPos){
                maxPos = farthest;
                jump++;
                if(maxPos>=nums.length-1) return jump;
            }
        }
        return jump;
    }
}
