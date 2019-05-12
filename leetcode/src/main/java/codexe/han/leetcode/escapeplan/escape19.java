package codexe.han.leetcode.escapeplan;

public class escape19 {
    public class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
   }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if(deethRecuresion(head,n)==n){
            return head.next;
        }
        return head;
    }
    public int deethRecuresion(ListNode node, int n){
        if(node==null){
            return 0;
        }
        int deepth = deethRecuresion(node.next,n);
        if(deepth==n){//上一层是第n个
            node.next = node.next.next;
        }
        return deepth+1;
    }
}
