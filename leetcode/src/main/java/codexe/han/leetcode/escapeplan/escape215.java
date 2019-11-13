package codexe.han.leetcode.escapeplan;

/**
 * 1.排序数组，取第k个数字
 * 2.快速排序思想，进行分治，base左边的元素是全都小于等于他的元素，
 * 右边是大于他的元素，直到找到base下标==nums.length-k的位置
 */
public class escape215 {
    public static void main(String[] args) {
        int[] nums = new int[]{7,6,5,4,3,2,1};
        findKthLargest(nums,2);
    }
    public static int findKthLargest(int[] nums, int k) {
        return quickSort(0,nums.length-1,nums,k);
    }
    public static int quickSort(int left, int right, int[] nums, int k){
     //   int base = nums[left];
        //莫名其妙，base选了中间数字以后，算法时间到了98%，估计可能和后台测试有关
        int base = nums[(left + right) / 2];
        nums[(left + right) / 2] = nums[left];
        nums[left] = base;

        int l=left;
        int r=right;
        //l永远指向小于等于base的数字
        //r永远指向大于base的数字，所以while循环要先开始r--
        while(l<r){
            while(l<r&&nums[r]>base){
                r--;
            }
            while(l<r&&nums[l]<=base){
                l++;
            }
            if(l<r){
                int tmp = nums[l];
                nums[l] = nums[r];
                nums[r] = tmp;
            }
        }
        nums[left] = nums[r];
        nums[r] = base;
        if(nums.length-k == r) return base;
        if(nums.length-k <r) return quickSort(left,r-1,nums,k);
        else return quickSort(r+1,right,nums,k);
    }


}

/**
 Find the kth largest element in an unsorted array. Note that it is the kth largest element in the sorted order, not the kth distinct element.

 Example 1:

 Input: [3,2,1,5,6,4] and k = 2
 Output: 5
 Example 2:

 Input: [3,2,3,1,2,4,5,5,6] and k = 4
 Output: 4
 Note:
 You may assume k is always valid, 1 ≤ k ≤ array's length.
 */