package codexe.han.leetcode.escapeplan;

/**
 * 数组被分成左右两部分，每部分都是升序的
 * 所以重点是找到分隔的位置
 * 思路就是先找mid 判断mid和start (或者mid和end)之间的关系，从而确定哪一边是有序的
 * start<mid, 左边一定是升序，判断start,target, mid之间的关系，target不在这中间的话，就在右边，
 * 然后mid+1, end，在调用上述逻辑，找target
 *
 * 二分查找调用left = mid-1 right = mid+1，因为更新后会出现left==right的情况，所以while循环一定是left<=right
 */
public class escape33 {
    public int search(int[] nums, int target) {
        return binarySearch(0, nums.length-1, target, nums);
    }
    public int binarySearch(int left, int right, int target, int[] nums){
        while(left<=right){//right left 移动都是=mid,所以会出现left==right的情况，跳出
            int mid = (left+right)/2;
            if(nums[mid] == target) return mid;
            if(nums[left]<=nums[mid]){
                //左边是连续递增的
                if(nums[left]<= target && target < nums[mid]){
                    //target在这中间 -> 二分查找正常逻辑
                    right = mid-1;
                }
                else{
                    //target不在这中间
                    left = mid+1;
                }
            }
            else{
                //右边是连续的
                if(nums[mid]< target && target <= nums[right]){
                    //target在这中间 -> 二分查找正常逻辑
                    left = mid+1;
                }
                else{
                    //target不在这中间
                    right = mid-1;
                }
            }
        }
        return -1;
    }
}
