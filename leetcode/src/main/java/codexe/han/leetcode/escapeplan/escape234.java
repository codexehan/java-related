package codexe.han.leetcode.escapeplan;

import java.util.List;

/**
 * 先找到链表中点
 * 链表分成两部分
 * 倒序后半部分
 * 从头对比两个链表是否palindrome
 */
public class escape234 {
    public boolean isPalindrome(ListNode head) {
        if(head==null) return true;
        ListNode slow = head;
        ListNode fast = head;
        ListNode prev = head;
        while(fast!=null&&fast.next!=null){
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = null;
        ListNode newHead = reverseNode(slow);
        while(head!=null && newHead!=null){
            if(head.val!=newHead.val) return false;
            head=head.next;
            newHead=newHead.next;
        }
        return true;
    }
    public ListNode reverseNode(ListNode node){
        //递归不好返回newead节点
        /*if(node.next==null) return node;
        ListNode nextNode = reverseNode(node.next);
        nextNode.next = node;*/
        ListNode newHead = node;
        ListNode newTail = node;
        node = node.next;
        while(node!=null){
            newTail.next = node.next;
            node.next = newHead;
            newHead = node;
            node = newTail.next;
        }
        return newHead;
    }
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
}
