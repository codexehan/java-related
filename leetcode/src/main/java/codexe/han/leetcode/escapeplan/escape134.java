package codexe.han.leetcode.escapeplan;

/**
 * 初始想法
 * 是否可以完成：统计过程不能出现负数
 * 找到连续区域中和的
 */
public class escape134 {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int sum=0;
        int localSum=0;
        int start=-1;
        for(int i=0;i<gas.length;i++){
            sum+=gas[i]-cost[i];
            localSum+=gas[i]-cost[i];
            if(localSum<0){
                //change start
                start=-1;
                localSum=0;
            }
            else if(start==-1){
                start =i;//找到第一个可能满足的位置
            }
        }
        return sum>=0?start:-1;
    }
}
