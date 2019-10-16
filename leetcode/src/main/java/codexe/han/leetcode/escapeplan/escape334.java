package codexe.han.leetcode.escapeplan;

public class escape334 {
    public boolean increasingTriplet(int[] nums) {
        if(nums.length<3) return false;
        int[] res = new int[3];
        res[0] = Integer.MAX_VALUE;
        res[1] = Integer.MAX_VALUE;
        for(int n: nums){
            if(n<=res[0]){//一定要考虑等于号，不然相等的会落到下一层判断
                res[0] = n;
            }
            else if(n<=res[1]){//能到这一步说明已经找到一个比n小的数字在res[0]
                res[1] = n;
            }
            else {//能到这一步说明已经找到两个比n小的数字在res[0] res[1]
                return true;
            }
        }
        return false;
    }
}
