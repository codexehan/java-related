package codexe.han.leetcode.escapeplan;

import lombok.val;

public class escape21 {
    //TODO:Test
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(0);
        head.next = null;
        ListNode p = head;
        ListNode p1 = l1;
        ListNode p2 = l2;
        while(p1!=null && p2!=null){
            if(p1.val < p2.val){
                p.next = new ListNode(p1.val);
                p1 = p1.next;
            }
            else{
                p.next = new ListNode(p2.val);
                p2 = p2.next;
            }
            p = p.next;
        }
        /*if(p1!=null){
            p.next = p1;
            return head.next;
        }
        if(p2!=null){
            p.next = p2;
            return head.next;
        }*/
        //合并上面的if语句
        p.next = p1!= null?p1:p2;
        return head.next;

    }
    public class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }
}
