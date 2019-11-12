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

/**
 Given a linked list, determine if it has a cycle in it.

 To represent a cycle in the given linked list, we use an integer pos which represents the position (0-indexed) in the linked list where tail connects to. If pos is -1, then there is no cycle in the linked list.



 Example 1:

 Input: head = [3,2,0,-4], pos = 1
 Output: true
 Explanation: There is a cycle in the linked list, where tail connects to the second node.
 */