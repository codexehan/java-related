package codexe.han.leetcode.escapeplan;

public class escape81 {
    public static void main(String[] args) {
        //[2,5,6,0,0,1,2] 3
        //[1,3,1,1,1] 3
        getRes(new int[]{1,3,1,1,1},3);
    }
    public static boolean getRes(int [] nums, int target){
        int left = 0, right = nums.length-1;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(target == nums[mid]){
                return true;
            }
            else if(nums[left]<nums[mid]){//左边是连续递增的
                if(nums[left]<=target&&target<nums[mid]){
                    right = mid-1;
                }
                else{
                    left = mid+1;
                }
            }
            else if(nums[mid]<nums[left]){//右边是连续递增的
                if(nums[mid]<target && target<=nums[right]){
                    left = mid+1;
                }
                else{
                    right = mid-1;
                }
            }
            else{//其他情况无法确定哪边是递增的 就只做left++
                left++;//这边就变成了串行遍历
            }
        }
        return false;
    }
    public boolean search(int[] nums, int target) {
        // note here end is initialized to len instead of (len-1)
        int start = 0, end = nums.length;
        while (start < end) {
            int mid = (start + end) / 2;
            if (nums[mid] == target) return true;
            if (nums[mid] > nums[start]) { // nums[start..mid] is sorted
                // check if target in left half
                if (target < nums[mid] && target >= nums[start]) end = mid;
                else start = mid + 1;
            } else if (nums[mid] < nums[start]) { // nums[mid..end] is sorted
                // check if target in right half
                if (target > nums[mid] && target < nums[start]) start = mid + 1;
                else end = mid;
            } else { // have no idea about the array, but we can exclude nums[start] because nums[start] == nums[mid]
                start++;
            }
        }
        return false;
    }
}
