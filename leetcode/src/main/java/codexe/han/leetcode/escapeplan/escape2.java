package codexe.han.leetcode.escapeplan;

public class escape2 {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode p1 = l1;
        ListNode p2 = l2;
        ListNode head = new ListNode(0);
        ListNode prev = head;
        int count = 0;
        int val1, val2 = 0;
        while(p2!=null||p1!=null||count!=0){
            val1 = p1==null?0:p1.val;
            val2 = p2==null?0:p2.val;
            int sum = val1 + val2 + count;
            if(sum>=10){
                sum = sum-10;
                count = 1;
            }
            else{
                count = 0;
            }
            ListNode current = new ListNode(sum);
            prev.next = current;
            prev = current;
            p1 = p1==null?null:p1.next;
            p2 = p2==null?null:p2.next;
        }
        prev.next= null;
        return head.next;
    }
}
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}