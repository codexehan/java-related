package codexe.han.leetcode.生产者消费者;

public class BaseBuffer<T> {

    private int capacity;

    private int currentSize;

    private int head;

    private int tail;

    private T[] buffer;

    public BaseBuffer(int capacity){
        this.capacity = capacity;
        this.buffer = (T[])new Object[capacity];
        this.head=0;
        this.tail=0;
        this.currentSize = 0;
    }

    public synchronized T doGet(){
        T ele = this.buffer[head];
        this.buffer[head]=null;
        if(head==this.capacity) head=0;
        this.currentSize--;
        return ele;
    }

    public synchronized void doPut(T ele){
        this.buffer[tail++] = ele;
        if(tail==this.capacity) tail=0;
        this.currentSize++;
    }

    public boolean isFull(){
        return this.capacity == this.currentSize;
    }
    public boolean isEmpty(){
        return this.currentSize == 0;
    }
}

class Buffer<T> extends BaseBuffer<T>{
    public Buffer(int capacity){
        super(capacity);
    }
    public synchronized T get() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        boolean wasFull = isFull();
        T ele = doGet();
        if(wasFull){
            notifyAll();
        }
        return ele;
    }
    public synchronized void put(T ele) throws InterruptedException {
        while(isFull()){
            wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(ele);
        if(wasEmpty){
            notifyAll();
        }
    }
}
