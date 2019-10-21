package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//考虑很多种情况
//思路一：二分查找，确定第kth个sum的大小，然后从头遍历，先取小于kth sum的pair，然后根据k的要求，取等于kth sum的pair
//思路二：构建最小堆 (head) 然后取得前n个
public class escape373 {
    public static void main(String[] args) {
     //   System.out.println(kSmallestPairs(new int[]{-10,-4,0,0,6}, new int[]{3,5,6,7,8,100},10));
     //   System.out.println(kSmallestPairs(new int[]{0,0,0,0,0}, new int[]{-3,22,35,56,76},22));
        System.out.println(kSmallestPairs(new int[]{1,7,11}, new int[]{2,4,6},3));
    }
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if(nums1.length==0||nums2.length==0) return res;
        int left = nums1[0]+nums2[0];
        int right = nums1[nums1.length-1]+nums2[nums2.length-1];
        int resCount;
        while(left<right){
            resCount = 0;
            int mid = left + (right-left)/2;
            for(int i=0;i<nums1.length;i++){
                for(int j=0;j<nums2.length&&(nums1[i]+nums2[j])<=mid;j++){
                    resCount++;
                }
            }
            //left最后的值是最后一组相加之和
            if(resCount<k) left = mid+1;
            else right = mid;
        }
        for(int i=0;i<nums1.length;i++){
            for(int j=0;j<nums2.length&&(nums1[i]+nums2[j])<left;j++){
                ArrayList tmp = new ArrayList();
                tmp.add(nums1[i]);
                tmp.add(nums2[j]);
                res.add(tmp);
            }
        }
        for(int i=0;i<nums1.length;i++){
            for(int j=0;res.size()<k&&j<nums2.length;j++){
                if((nums1[i]+nums2[j])==left) {
                    ArrayList tmp = new ArrayList();
                    tmp.add(nums1[i]);
                    tmp.add(nums2[j]);
                    res.add(tmp);
                }
            }
        }
        return res;
    }
}
