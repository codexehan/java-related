package codexe.han.leetcode.escapeplan;

/**
 * 可以买入和交易多次 求最大利润
 * 每次有新的最大利润，就更新min max从当前位置开始重新算后面的最大利润
 * 一直相加，得到最后结果
 */
public class escape122 {
    public int maxProfit(int[] prices) {
        int max = 0;
        if(prices.length!=0) {
            int localmin = prices[0];
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > localmin) {
                    max += prices[i] - localmin;
                    localmin = prices[i];
                }
                localmin = Math.min(localmin, prices[i]);
            }
        }
        return max;
    }
}
