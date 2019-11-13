package codexe.han.leetcode.escapeplan;

/**
 * 分治算法
 * 比较数组右上角的数字rightTop, target>rightTop的话，去掉该行
 * target<rightTop的话，去掉该列
 */
public class escape240 {
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
    public int binarySearch(int[] nums, int target){
        int left = 0;
        int right = nums.length-1;
        int mid = (left+right)/2;
        while(left<right){
            if(target == nums[mid]){
                return target;
            }
            else if(target<nums[mid]){
                right = mid-1;
            }
            else {
                left = mid;
            }
        }
        return nums[left];
    }
}
/**
 Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:

 Integers in each row are sorted in ascending from left to right.
 Integers in each column are sorted in ascending from top to bottom.
 Example:

 Consider the following matrix:

 [
 [1,   4,  7, 11, 15],
 [2,   5,  8, 12, 19],
 [3,   6,  9, 16, 22],
 [10, 13, 14, 17, 24],
 [18, 21, 23, 26, 30]
 ]
 Given target = 5, return true.

 Given target = 20, return false.
 */