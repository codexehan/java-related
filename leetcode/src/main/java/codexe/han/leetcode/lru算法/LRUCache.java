package codexe.han.leetcode.lru算法;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * hashtable 是线程安全的
 * hashmap 允许null key null value
 * @param <K>
 * @param <V>
 */
public class LRUCache<K,V> {
    private Map<K,LRUNode<K,V>> container;
    private int capacity;
    private int currentSize;
    private LRUNode<K,V> head;
    private LRUNode<K,V> tail;

    public LRUCache(int capacity){
        this.capacity = capacity;
        container = new HashMap<>();
        this.currentSize = 0;
    }

    public synchronized void put(K key, V value){
        LRUNode<K,V> node = new LRUNode<>(null,null,key,value);
        if(currentSize == capacity){
            removeTail();
        }
        addHead(node);
        currentSize++;
        this.container.put(key,node);
    }

    public synchronized V get(K key){
        if(container.containsKey(key)) {
            LRUNode<K,V> node = this.container.get(key);
            move2Head(node);
            return node.value;
        }
        return null;
    }

    private void removeTail(){
        this.container.remove(tail.key);
        tail = tail.prev;
        tail.next = null;
        currentSize--;
    }

    private void addHead(LRUNode<K,V> node){
        if(currentSize == 0){
            head = node;
            tail = node;
        }
        else{
            node.next = head;
            head.prev = node;
            head = node;
        }
        container.put(node.key,node);
    }

    private void move2Head(LRUNode<K,V> node){
        if(head!=node){
            node.prev.next = node.next;
            node.next.prev = node.prev;

            node.next = head;
            node.prev = null;
            head = node;
        }
    }

    public static void main(String[] args) {
        LRUCache<Integer,Integer> lruCache = new LRUCache(10);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.put(4,4);
        lruCache.put(5,5);
        lruCache.put(6,6);
        lruCache.put(7,7);
        lruCache.put(8,8);
        lruCache.put(9,9);
        lruCache.put(10,10);

        lruCache.getLRUList();

        System.out.println(lruCache.get(8));

        lruCache.getLRUList();

        lruCache.put(11,11);

        lruCache.getLRUList();


    }

    public void getLRUList(){
        List<Integer> keyList = new ArrayList<>();
        LRUNode p = head;
        while(p!=null){
            keyList.add((Integer) p.key);
            p = p.next;
        }
        System.out.println(keyList);
    }

}
class LRUNode<K,V>{
    K key;
    V value;
    LRUNode prev;
    LRUNode next;
    public LRUNode(LRUNode<K,V> prev, LRUNode<K,V> next, K key, V value){
        this.prev = prev;
        this.next = next;
        this.key = key;
        this.value = value;
    }
}


