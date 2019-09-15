package codexe.han.leetcode.escapeplan;

/**
 * 基本思想1
 * hashtable存储各节点的复制node 但是会消耗存储空间
 *
 * 基本思想2
 * 为了避免使用hashtable
 * 每个节点的next指向复制节点
 * 每个复制节点的random指向原始节点的random的next
 * 每个复制节点next指向原来next的next
 */
public class escape138 {
    public Node copyRandomList(Node head) {
        Node p = head;

        //插入每个节点的复制节点
        while(p!=null){
            Node dup = new Node(p.val,p.next,p.random);
            p.next = dup;
            p = dup.next;
        }
        //每个复制节点的random指向原始节点的random的next
        p=head.next;
        while(p !=null){
            p.random = p.random.next;
            p=p.next==null?null:p.next.next;
        }
        //每个复制节点next指向原来next的next
        //恢复原始节点
        p=head;
        Node q = head.next;
        Node dupHead = q;
        while(p!=null && q!=null){
            p.next = p.next==null?null:p.next.next;
            q = q.next==null?null:q.next.next;
        }

        return dupHead;

    }
}

class Node {
    public int val;
    public Node next;
    public Node random;

    public Node() {}

    public Node(int _val,Node _next,Node _random) {
        val = _val;
        next = _next;
        random = _random;
    }
};
