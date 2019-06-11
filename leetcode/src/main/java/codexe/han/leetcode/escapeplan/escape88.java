package codexe.han.leetcode.escapeplan;

/**
 * 倒着做合并
 *
 * 不会做的时候，就逆向思维考虑一下
 */
public class escape88 {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1=m-1, p2=n-1;
        int p=nums1.length-1;
        for(;p1>=0&&p2>=0;){
            if(nums2[p2]>nums1[p1]) {
                nums1[p--] = nums2[p2--];
            }
            else{
                nums1[p--] = nums1[p1--];
            }
        }
        while(p2>=0){
            nums1[p--] = nums2[p2--];
        }
    }
}
