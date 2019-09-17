package codexe.han.leetcode.escapeplan;

/**
 * 归并排序
 *
 * 如何找到中心位置，一个快的，一个慢的，快的到达莫为以后，慢的最后就落在中心位置了
 */
public class escape148 {
    public ListNode sortList(ListNode head) {
        if(head==null || head.next==null) return head;
        //find mid node
        ListNode fast = head;
        ListNode slow = head;
        ListNode prev = head;
        while(fast!=null){
            prev = slow;
            slow = slow.next;
            fast = fast.next==null?fast.next:fast.next.next;
        }
        prev.next = null;

        /**
         * 这里是关键 容易出错
         * sortList会返回排好序以后的头部节点，然后用新的头部节点尽心merge
         */
        ListNode newHead1 = sortList(head);
        ListNode newHead2 = sortList(slow);
        return merge(newHead1, newHead2);

    }

    public ListNode merge(ListNode left, ListNode right){
        //merge two sorted list
        ListNode head = new ListNode(0);
        ListNode p = head;
        while(left!=null&&right!=null){
            if(left.val<right.val){
                p.next = left;
                left = left.next;
            }
            else{
                p.next = right;
                right = right.next;
            }
            p = p.next;
        }
        if(left!=null){
            p.next = left;
        }
        if(right!=null){
            p.next = right;
        }
      //  p.next=null;//不需要,原始节点会有next null信息
        return head.next;


    }

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
}