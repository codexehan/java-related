package codexe.han.leetcode.escapeplan;
//题目中必然成立的条件
//左上角是最小值 右下角是最大值
//二分查找分为两种，一种是数组有序，根据下标进行二分查找
//另一种是，直到最大值，最小值，根据数值进行二分查找
public class escape387 {
    public int kthSmallest(int[][] matrix, int k) {
        int left = matrix[0][0];
        int right = matrix[matrix.length-1][matrix.length-1];
        int res = 0;
        while(left<right){
            int mid = left + (right-left)/2;
            res = 0;
            for(int i=0;i<matrix.length;i++){
                for(int j=0;j<matrix.length&&matrix[i][j]<=mid;j++){
                    res++;
                }
            }
            //这边的处理逻辑就考虑left,right,和kth数字的关系，不用考虑中间的其他数字，假设kth的数字正好在left right中间，没有其他数字了
            //这个时候，在取得mid，如果不够k个，说明该数字在mid右边，所以 left = mid+1
            //如果还是k个，说明该数字在mid左边或者就是mid，所以 right = mid
            //这样不断地修正，最后left right相遇的地方一定是第kth位置
            if(res<k) left = mid+1;
            else right = mid;//max = mid-1;
        }
        return left;
    }
}
