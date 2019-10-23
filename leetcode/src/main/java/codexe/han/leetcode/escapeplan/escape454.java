package codexe.han.leetcode.escapeplan;

import java.util.HashMap;
import java.util.Map;
//计算前两个数组相加的和，存到map中
//计算另外两个数组相加的和，从map中对应的和的负数
public class escape454 {
    public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
        int n = A.length;
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                int sum = A[i] + B[j];
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                int sum = C[i] + D[j];
                count += map.getOrDefault(-1 *sum, 0);
            }
        }
        return count;
    }
}
