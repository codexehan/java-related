package codexe.han.leetcode.escapeplan;

/**
 * 只能交易一次 获取最大利润
 * 在当前数额的时候，更新新的max
 * 然后计算出一个最大利润，和之前的最大利润比较
 * 然后比较最小值，更新新的min
 */
public class escape121 {
    public int maxProfit(int[] prices) {
        int min = prices[0];
        int max = prices[0];
        int maxp = 0;
        for(int i=1;i<prices.length;i++){
            max = Math.max(max, prices[i]);
            maxp = Math.max(max-min, maxp);
            min = Math.min(min,prices[i]);
        }
        return maxp;
    }
}
