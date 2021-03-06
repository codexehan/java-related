package codexe.han.leetcode.escapeplan;

/**
 * 只能交易一次 获取最大利润
 * 在当前数额的时候，更新新的max  -> 不需要更新最大的max，只需要一直记录最小值min，每次都用当前price减去之前的min来计算
 * 注意 只有当前数值变成最大值以后 才然后计算出一个最大利润（因为最大值必须出现在最小值后面），和之前的最大利润比较
 * 然后比较最小值，更新新的min
 */
public class escape121 {
    public int maxProfit(int[] prices) {
        int maxp = 0;
        if(prices.length!=0) {
            int min = prices[0];
            for (int i = 1; i < prices.length; i++) {
                maxp = Math.max(prices[i] - min, maxp);
                min = Math.min(min, prices[i]);
            /*if(max>prices[i]){
                max=prices[i];
                maxp = Math.max(max-min, maxp);
            }
            else {
                min = Math.min(min, prices[i]);
            }*/
            }
        }
        return maxp;
    }
}

/**
 Say you have an array for which the ith element is the price of a given stock on day i.

 If you were only permitted to complete at most one transaction (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.

 Note that you cannot sell a stock before you buy one.

 Example 1:

 Input: [7,1,5,3,6,4]
 Output: 5
 Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
 Not 7-1 = 6, as selling price needs to be larger than buying price.
 Example 2:

 Input: [7,6,4,3,1]
 Output: 0
 Explanation: In this case, no transaction is done, i.e. max profit = 0.
 */