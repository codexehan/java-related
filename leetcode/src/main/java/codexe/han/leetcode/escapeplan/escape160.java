package codexe.han.leetcode.escapeplan;

/**
 * 假设c是重复的位置
 * 如果list1长度是a+c list2长度是b+c
 * 如果a先遍历list1 a+c 再转到list2 b+c a+c+b+c
 * 如果b先遍历list2 b+c 再转到list1 a+c b+c+a+c
 *
 * a+c+b = b+c+a 所以两个指针肯定会在这个位置遇见，假设有交集的话
 * 如果c=0 就是没有交集null， 连个指针在结尾处遇见
 *
 */
public class escape160 {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if(headA==null||headB==null) return null;
        ListNode a = headA;
        ListNode b = headB;
        while(a!=b){
            a = a==null?headB:a.next;
            b = b==null?headA:b.next;
        }
        return a;
    }
    class ListNode {
          int val;
         ListNode next;
         ListNode(int x) {
             val = x;
             next = null;
         }
    }
}
