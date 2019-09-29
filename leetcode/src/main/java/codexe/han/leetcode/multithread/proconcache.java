package codexe.han.leetcode.multithread;

/**
 * Thread safe cache
 */
public class proconcache {
    private int[] cache;
    private int currentSize;
    public proconcache(int capacity){
        this.cache = new int[capacity];
        this.currentSize = 0;
    }

    /**
     * 生产
     * @return
     */
    public boolean put(int n) throws InterruptedException {
        //1.缓存满了的话，需要阻塞
        if(this.currentSize==this.cache.length){
            wait();
        }
        else{
            currentSize++;
            this.cache[currentSize-1] = n;
            notifyAll();
            return true;
        }
    }
}

class BaseCache<T>{
    private int head;
    private int currentSize;
    private T[] cache;
    public BaseCache(int capacity){
        this.cache = (T[]) new Object[capacity];
        this.currentSize = 0;
        this.head = 0;
    }

    public
}
