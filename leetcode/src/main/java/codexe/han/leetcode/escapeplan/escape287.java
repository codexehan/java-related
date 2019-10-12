package codexe.han.leetcode.escapeplan;

/**
 * 思路和链表存在环形位置，然后找到该环形起始节点
 * 数组的数字代表数组下标的纸箱关系
 * 要点是，将数组里面的数字当做是数组的index进行移动，这样才能够达到环的效果
 * 环的长度是n
 * 第一次相遇：slow: m+x fast: m+x+n
 * 2(m+x)=m+x+n
 * n=m+x
 * m = n-x; slow从0开始 fast从第一次相遇的节点开始，一步一步走，最后会再次相遇，相遇点就是重复节点
 */
public class escape287 {
    public int findDuplicate(int[] nums) {
        int slow = nums[0];
        int fast = nums[0];
        do{
            slow = nums[slow];
            fast = nums[nums[fast]];
        }while(slow!=fast);
        slow = nums[0];
        while(slow!=fast){
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}
