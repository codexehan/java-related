package codexe.han.leetcode.escapeplan;

public class escape141 {
    public boolean hasCycle(ListNode head) {
        ListNode p = head;
        ListNode q = head;
        while(p!=null && q!=null){
            p = p.next;
            q = q.next==null?null:q.next.next;
            if(p==q && p!=null) return true;

        }
        return false;
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
