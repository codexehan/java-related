package codexe.han.leetcode.escapeplan;

/**
 * 倒着做合并
 *
 * 不会做的时候，就逆向思维考虑一下
 */
public class escape88 {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1=0, p2=0;
        for(;p1<m;p1++){
            if(nums1[p1]>nums2[p2]) {
                swap(nums1, p1, nums2, p2);
                p1++;
            }
        }
    }
    public void swap(int[] nums1, int p1, int[] nums2, int p2){
        int tmp = nums1[p1];
        nums1[p1] = nums2[p2];
        nums2[p2] = tmp;
    }
}
