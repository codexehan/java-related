package codexe.han.leetcode.escapeplan;

/**
 * 原题：
 * A peak element is an element that is greater than its neighbors.
 *
 * Given an input array nums, where nums[i] ≠ nums[i+1], find a peak element and return its index.
 *
 * The array may contain multiple peaks, in that case return the index to any one of the peaks is fine.
 *
 * You may imagine that nums[-1] = nums[n] = -∞.
 *
 * 二分查找
 * You may imagine that nums[-1] = nums[n] = -∞
 * -1 n都是无限小数
 *
 * 如果mid-1>mid ，mid向左一直是上升的话，那么0就是peak；如果mid向左不是一直上升的话，那么就肯定会先上升在下降，所以一定存在peak
 * 如果mid-1<mid, mid向右一直上升的话，那么n-1就是peak；如果mid向右不是一直上升，那就会mid-1 - n-1就是先上升后下降，肯定存在peak
 */
public class escape162 {
    public int findPeakElement(int[] nums) {
        int left = 0;
        int right = nums.length-1;
        while(left<right){

            int mid = (left + right) / 2;
            if(nums[mid] < nums[mid + 1]) left = mid + 1;//mid+1可能是peak
            else right = mid;//mid可能是peak

/*
            int mid = (left+right)/2;
            if(mid==0||mid==nums.length-1||nums[mid-1]<nums[mid]&&nums[mid]<nums[mid+1]){
                return mid;
            }
            else if(nums[mid-1]>nums[mid]){
                right = mid-1;//mid-1可能是peak
            }
            else{
                left = mid+1;//mid可能是peak
            }*/
        }
        return left;

    }

}
