package codexe.han.leetcode.multithread;

/**
 * Thread safe cache
 */
public class proconcache {

}

class BaseCache<T>{
    private int head;
    private int tail;
    private int currentSize;
    private T[] cache;
    public BaseCache(int capacity){
        this.cache = (T[]) new Object[capacity];
    }

    protected synchronized void doPut(T t){
        this.cache[tail++] = t;
        if(tail == this.cache.length-1){
            tail = 0;
        }
        currentSize++;
    }

    protected synchronized T doTake(){
        T t = this.cache[head];
        this.cache[head] = null;
        head++;
        if(head == this.cache.length-1){
            head = 0;
        }
        currentSize--;
        return t;
    }

    protected synchronized boolean isFull(){
        return currentSize==this.cache.length;
    }

    protected synchronized boolean isEmpty(){
        return currentSize == 0;
    }
}

class CacheThreadSafe<T> extends BaseCache<T>{

    public CacheThreadSafe(int capacity) {
        super(capacity);
    }

    /**
     * 只有两种情况，会wait，要么是满了，要么是空了
     * 所以，只有当满->不满，唤醒
     * 空->不空，唤醒
     */

    public synchronized void put(T t) throws InterruptedException {
        //条件为此 是否满
        while(isFull()){
            wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(t);
        if(wasEmpty){//空->非空
            notifyAll();
        }
    }

    public synchronized T take() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        boolean wasFull = isFull();
        T t = doTake();
        if(wasFull){//满->不满
            notifyAll();
        }
        return t;
    }
}
