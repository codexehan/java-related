package codexe.han.leetcode.escapeplan;

/**
 * 这道题目和240的不同点在于，该题目是全局有序的，可以用二分查找来解决
 * 算法复杂度是O(nlogn)
 * 所以还是使用分治算法解决
 */
public class escape74 {
    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix==null||matrix.length==0||matrix[0].length==0) return false;
        int i=0;
        int j=matrix[0].length-1;
        while(i<matrix.length&&j>=0){
            if(matrix[i][j]>target) j--;//remove current column
            else if(matrix[i][j]<target) i++;
            else return true;
        }
        return false;
    }
}
