package codexe.han.leetcode.escapeplan;

/**
 * Input: [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * 排序
 * tmpStart, tmpEnd记录现在未加入最后结果的区间，
 * 如果下一个数组[0]在tmpStart 和 tmpEnd中间， 那么比较 [1]和tmpEnd大小，取最大值，赋给tmpEnd
 * 如果不在这个区间内，就讲tmpStart, tmpEnd加入到最新结果，然后tmpStart = [0] tmpEnd = [1]
 */
public class escape56 {
    public int[][] merge(int[][] intervals) {
        return null;
    }
}
