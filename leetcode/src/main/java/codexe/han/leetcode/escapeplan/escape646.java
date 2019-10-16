package codexe.han.leetcode.escapeplan;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 上一个pair的结尾一定要小于下一个chain的开头
 * 所以最后组成chain的pair一定是按照pair结尾数字由小到大排序的
 *
 * 和贪心算法一样
 *
 * 这道题目和increasing sequence不同的地方在于，不需要维护sequence的顺序，所以直接排序然后统计就可以了。
 */
public class escape646 {
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs, Comparator.comparingInt(a -> a[1]));
        int res = 0, n = pairs.length-1;
        for(int i=0;i<=n;i++){
            res++;
            int currentEnd = pairs[i][1];
            int j = i;
            while(++j<=n&&pairs[j][0]<=currentEnd){
                i++;
            }
        }
        return res;
    }
}
