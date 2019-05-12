package codexe.han.leetcode.escapeplan;

public class escape11 {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length-1;
        int maxArea = 0;
        while(left< right){
            int hl = height[left];
            int hr = height[right];
            int h = Math.min(hl,hr);
            int w = right - left;
            maxArea = Math.max(h*w, maxArea);
            if(hl<hr){
                do{
                    left++;
                }while(left<right&&hl>=height[left]);
            }
            else{
                do{
                    right--;
                }while(left<right&&hr>=height[right]);
            }
        }
        return maxArea;
    }
}
