package codexe.han.leetcode.escapeplan;


/**
 * 思路1
 * 降维成一维数组，然后利用quick思想找到第kth小的数字
 *
 * 思路2
 * 数组知道最大值最小值，可以知道mid的大小
 * 每次遍历数组
 */
public class escape378 {
    public int kthSmallest(int[][] matrix, int k) {
        int left = matrix[0][0];
        int right = matrix[matrix.length - 1][matrix.length - 1];
        int res = 0;
        while (left < right) {
            int mid = left + (right - left) / 2;
            res = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length && matrix[i][j] <= mid; j++) {
                    res++;
                }
            }
            //这边的处理逻辑就考虑left,right，假设kth的数字正好在left right中间，没有其他数字了
            //这个时候，在取得mid，如果不够k个，说明该数字在mid右边，所以 left = mid+1
            //如果还是k个，说明该数字在mid左边或者就是mid，所以 right = mid
            //这样不断地修正，最后left right相遇的地方一定是第kth位置
            if (res < k) left = mid + 1;
            else right = mid;//max = mid-1;
        }
        return left;
    }
}

/**
 Given a n x n matrix where each of the rows and columns are sorted in ascending order, find the kth smallest element in the matrix.

 Note that it is the kth smallest element in the sorted order, not the kth distinct element.

 Example:

 matrix = [
 [ 1,  5,  9],
 [10, 11, 13],
 [12, 13, 15]
 ],
 k = 8,

 return 13.
 */
