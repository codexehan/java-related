package codexe.han.leetcode.escapeplan;

import java.util.Arrays;

/**
 * 一开始的想法是找到数组的中位数，然后根据中位数对数组划分为两部分。大的那一部分放在索引为奇数的位置(1，3，5，7，9…)，小的那一部分放在索引为偶数的位置(0，2，4，6，8…)。（这里是可以随意放的）
 * 这个想法是对的，但还不够。比如当数组为[4,5,5,6]时，得到的结果就是[4,5,5,6]，这不符合题目要求，正确的结果应该是[5,6,4,5]。这里问题出在两部分数中相同大小的数放在一起了
 *
 * 为了避免这个问题，直观上来说，需要把较小的那一部分中比较大的数和较大的那一部分中比较小的数放的越远越好。所以就是小的当中最大的数字要和大的当中最大的数字尽可能放在一起。所以要先排序
 * 不排序的话，还有一种方法，就是和中位数相等的数字间隔放入
 */
public class escape324 {
    public static void main(String[] args) {
        int[] nums = new int[]{5,3,1,2,6,7,8,5,5};
        wiggleSort2(nums);
    }
    /**
     * 算法复杂度O(nlogn)
     */
    public void wiggleSort(int[] nums) {
        Arrays.sort(nums);
        int[] tmp = new int[nums.length];
        int large = nums.length-1;
        //中位数数字选取的时候，要注意选取的位置
        //数组偶数位置一定大于等于奇数位置，所以small区域数字一定大于或者large为个数
        //数组偶数位置等于奇数位置的话，长度是偶数，中位数要
        int small = (0+nums.length-1)/2;//(left+right)/2
        for(int i=0;i<nums.length;i++){
            tmp[i] = (i&1)==0?nums[small--]:nums[large--];//小于等于中位数的数字放在偶数位，不然为数组outofbound
        }
        for(int i=0;i<nums.length;i++){
            nums[i] = tmp[i];
        }
    }

    /**
     *解释取中位数的原则
     */
    public static void wiggleSort1(int[] nums) {
        Arrays.sort(nums);
        int[] tmp = new int[nums.length];
        int large = nums.length-1;
        int small = (nums.length)/2;//如果非要用这个公式取中位数的话
        //1.长度为偶数的时候，small个数会比large个数多2个，但是偶数位置个数是等于奇数位置个数的。small需要减少一个
        //2.长度为奇数的时候，small个数回避large个数多1个，但是偶数位置个数比技术位置个数多1个。正好
        for(int i=0;i<nums.length;i++){
            if((nums.length&1)==0){
                tmp[i] = (i&1)==0?nums[--small]:nums[large--];
            }
            else{
                tmp[i] = (i&1)==0?nums[small--]:nums[large--];
            }
        }
        for(int i=0;i<nums.length;i++){
            nums[i] = tmp[i];
        }
    }

    /**
     * 选择排序的思想，找到中位数O(n)
     * 这个思路就是尽量让和中位数相等的数字分开放
     * 这边是分别放在两端 越远越好
     */
    public static void wiggleSort2(int[] nums){
        int mid = findKthLargestNum(nums,(nums.length-1)/2+1,0,nums.length-1);//(nums.length-1)/2是中位数位置，但是对应的他是第(nums.length-1)/2+1大的数
        int[] tmp = new int[nums.length];
        //int odd=1, even=0;//如果odd和even都从一个端开始，最后可能会有连续两个相同中位数靠在一起的情况
        //如果是even 从左往右，odd从右向左，那么可能会导致中间部分是相同的中位数
        //如果odd从左往右，even从右向左，那么中间部分就会被先填上 比如[4,5,5,6]
       // int even = 0;
        //int odd = (nums.length&1)==1?nums.length-2:nums.length-1;
        int even = (nums.length&1)==0?nums.length-2:nums.length-1;
        int odd = 1;
        for(int i=0;i<nums.length;i++){
            if(nums[i]<mid){
                tmp[even] = nums[i];
                even = even-2;
            }
            else if(nums[i]>mid){
                tmp[odd] = nums[i];
                odd = odd+2;
            }
        }
        while(even>=0){
            tmp[even] = mid;
            even = even-2;
        }
        while(odd<nums.length){
            tmp[odd] = mid;
            odd = odd+2;
        }
        for(int i=0;i<nums.length;i++){
            nums[i] = tmp[i];
        }
    }
    public static int findKthLargestNum(int[] nums, int k, int left, int right){
        int base = nums[left];
        int l = left;
        int r = right;
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
        if(r==nums.length-k) return base;
        if(r> nums.length-k) return findKthLargestNum(nums,k,left,r-1);
        else return findKthLargestNum(nums,k,r+1,right);

    }
}
