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
