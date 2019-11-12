package codexe.han.leetcode.escapeplan;

/**
 * 只要gas的总和比cost的总和多，那么就一定存在解
 *
 * 在这个统计过程中，要不断统计以i为开头的区域 gas cost差，如果小于0需要替换start和小区域sum，最后start的位置就是起始位置
 *
 *
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

/**
 There are N gas stations along a circular route, where the amount of gas at station i is gas[i].

 You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from station i to its next station (i+1). You begin the journey with an empty tank at one of the gas stations.

 Return the starting gas station's index if you can travel around the circuit once in the clockwise direction, otherwise return -1.

 Note:

 If there exists a solution, it is guaranteed to be unique.
 Both input arrays are non-empty and have the same length.
 Each element in the input arrays is a non-negative integer.
 Example 1:

 Input:
 gas  = [1,2,3,4,5]
 cost = [3,4,5,1,2]

 Output: 3

 Explanation:
 Start at station 3 (index 3) and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
 Travel to station 4. Your tank = 4 - 1 + 5 = 8
 Travel to station 0. Your tank = 8 - 2 + 1 = 7
 Travel to station 1. Your tank = 7 - 3 + 2 = 6
 Travel to station 2. Your tank = 6 - 4 + 3 = 5
 Travel to station 3. The cost is 5. Your gas is just enough to travel back to station 3.
 Therefore, return 3 as the starting index.
 */