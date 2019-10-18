package codexe.han.leetcode.escapeplan;

/**
 Input: 2->1->3->5->6->4->7->NULL
 Output: 2->3->6->7->1->5->4->NULL

 Input: 1->2->3->4->5->NULL
 Output: 1->3->5->2->4->NULL
 */
public class escape328 {
    public static void main(String[] args) {

    }
    public ListNode oddEvenList(ListNode head) {
        if(head==null) return head;
        ListNode oddHead = head;
        ListNode evenHead = head.next;
        ListNode even  = evenHead;
        //odd个数一定大于等于even个数  这个规则在wiggle sort里面也是适用的
        while(oddHead!=null&&oddHead.next!=null&&oddHead.next.next!=null){
            oddHead.next = oddHead.next.next;
            oddHead = oddHead.next;

            if(evenHead!=null&&evenHead.next!=null) {
                evenHead.next = evenHead.next.next;
                evenHead = evenHead.next;
            }
        }
        oddHead.next = even;
        /*while(oddHead!=null&&oddHead.next!=null&&oddHead.next.next!=null){
            oddHead.next = oddHead.next.next;
            oddHead = oddHead.next;
        }*/
        //不能分开做，这一步的时候 evenHead.next.next已经发生了变化
        /*while(evenHead!=null&&evenHead.next!=null){
            evenHead.next = evenHead.next.next;
            evenHead = evenHead.next;
        }
        oddHead.next = even;*/


        return head;

    }
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
}
