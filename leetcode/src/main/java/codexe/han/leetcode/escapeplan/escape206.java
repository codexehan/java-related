package codexe.han.leetcode.escapeplan;

/**
 * 如果不使用递归，使用while循环的话，就是增加一个temp节点 做交换
 */
public class escape206 {
    ListNode newHead;
    public ListNode reverseList(ListNode head) {
        if(head==null) return head;
        reverse(head);
        head.next = null;
        return newHead;
    }
    public ListNode reverse(ListNode node){
        if(node.next==null) {
            newHead = node;
            return node;
        }
        ListNode nxtNode = reverse(node.next);
        nxtNode.next = node;
        return node;
    }

  public class ListNode {
     int val;
     ListNode next;
     ListNode(int x) { val = x; }
    }
}
