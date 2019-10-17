package codexe.han.leetcode.escapeplan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//days = [1,2,3,4,5,6,7,8,9,10,30,31], costs = [2,7,15]
//举个例子，30天的票价比7天的便宜的话，那么6d，要买三十天的票
//dp[i] = Math.min(dp[Math.max(i-1,0)]+costs[0],dp[Math.max(i-7,0)]+costs[1])
//dp[i] = Math.min(dp[Math.max(i-30,0)]+costs[2],dp[i])
//如果当天不需要旅行，那么花费就等于昨天的价格
public class escape983 {

    /**
     [1,4,6,7,8,20]
     [7,2,15]
     */
    public static void main(String[] args) {
        mincostTickets(new int[]{1,4,6,7,8,20},new int[]{7,2,15});
    }

    public static int mincostTickets(int[] days, int[] costs) {
        Set<Integer> daysSet = new HashSet<>();
        int maxDay = Integer.MIN_VALUE;
        for(int d : days){
            daysSet.add(d);
            maxDay = Math.max(d,maxDay);
        }
        int[] dp = new int[maxDay+1];
        dp[0] = 0;
        for(int i=1;i<=maxDay;i++){
            //如果当天不需要旅行，那么花费就等于昨天的价格
            if(!daysSet.contains(i)){
                dp[i] = dp[i-1];
            }
            else{
                int cur = dp[i - 1] + costs[0];
                cur = Math.min(cur, costs[1] + dp[Math.max(0, i - 7)]);
                cur = Math.min(cur, costs[2] + dp[Math.max(0, i - 30)]);
                dp[i] = cur;
                //不能用下面这种比较方式，举个例子，30天的票价比7天的便宜的话，那么6d，要买三十天的票
                /*if(i>=1){
                    dp[i] = dp[i-1]+costs[0];
                }
                if(i>=7){
                    dp[i] = Math.min(dp[i],dp[i-7]+costs[1]);
                }
                if(i>=30){
                    dp[i] = Math.min(dp[i],dp[i-30]+costs[2]);
                }*/
            }
        }
        return dp[maxDay];
    }

}
