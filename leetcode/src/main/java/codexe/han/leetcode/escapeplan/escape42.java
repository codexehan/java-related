package codexe.han.leetcode.escapeplan;
//Input: [0,1,0,2,1,0,1,3,2,1,2,1]
//Output: 6
public class escape42 {
    public int trap(int[] height) {
        int res = 0;
        int left = 0;
        int right = height.length-1;
        while(left<right){
            int hl = height[left];
            int hr = height[right];
            if(hl<hr){
                while(height[++left]<hl){
                    res+=hl-height[left];
                }
            }
            else{
                while(height[--right]<hr){
                    res+=hr-height[right];
                }
            }
        }
        return res;
    }
}
