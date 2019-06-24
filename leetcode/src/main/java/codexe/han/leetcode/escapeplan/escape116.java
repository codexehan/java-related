package codexe.han.leetcode.escapeplan;

public class escape116 {
    public Node connect(Node root) {
        if(root==null) return null;
        if(root.left!=null) root.left.next=root.right;
        if(root.next!=null && root.right!=null) root.right.next=root.next.left;
        connect(root.left);
        connect(root.right);
        return root;

    }
    public Node bft(Node node){
       if(node==null) return null;
       if(node.left!=null) node.left.next = node.right;
       if(node.next!=null) node.right.next = node.next.left;//perfect binary tree
       bft(node.left);
       bft(node.right);
       return node;
    }

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val,Node _left,Node _right,Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

}
