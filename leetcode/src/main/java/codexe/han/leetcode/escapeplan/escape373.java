package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class escape373 {
    public static void main(String[] args) {
        /**
         [-10,-4,0,0,6]
         [3,5,6,7,8,100]
         10
         */
        kSmallestPairs(new int[]{-10,-4,0,0,6}, new int[]{3,5,6,7,8,100},10);
    }
    public static List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
        List<List<Integer>> res = new ArrayList<>();
        if(nums1.length==0||nums2.length==0) return res;
        int left = nums1[0]+nums2[0];
        int right = nums1[nums1.length-1]+nums2[nums2.length-1];
        while(left<right){
            res.clear();
            int mid = left + (right-left)/2;
            for(int i=0;i<nums1.length;i++){
                for(int j=0;j<nums2.length&&(nums1[i]+nums2[j])<=mid;j++){
                    ArrayList tmp = new ArrayList();
                    tmp.add(nums1[i]);
                    tmp.add(nums2[j]);
                    res.add(tmp);
                }
            }
            //left最后的值是最后一组相加之和
            if(res.size()==k) return res;
            if(res.size()<k) left = mid+1;
            else right = mid;
        }
        return res;
    }
}
