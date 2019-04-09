package codexe.han.concurrency.tools;

import java.util.concurrent.atomic.AtomicInteger;

public class BaseBuffer<T> {
    private final T[] buffer;
    private int currentSize;
    private int head;
    private int tail;

    public BaseBuffer(int capacity){
        this.buffer = (T[])new Object[capacity];
    }
    //final 修饰函数，不允许重写，并且执行速度更快，不需要动态编译
    public synchronized final boolean isFull(){
        return currentSize == buffer.length;
    }

    public synchronized final boolean isEmpty(){
        return currentSize == 0;
    }

    public synchronized final void doPut(T t){
        buffer[tail] = t;
        if(++tail == buffer.length){
            tail = 0;
        }
        currentSize++;
    }
    public synchronized final T doTake(){
        T t = buffer[head];
        buffer[head] = null;
        if(++head == buffer.length){
            head = 0;
        }
        currentSize--;
        return t;
    }
}

class BufferWaitNotify<T> extends BaseBuffer<T>{

    public BufferWaitNotify(int capacity) {
        super(capacity);
    }

    //这种做法每次put都会唤醒
    //但是只有当缓存从空变为非空 或者 从满变为不满 才会需要唤醒 因为 只有空或者满的时候才会有线程进行等待
    //所以只需要在上述两种情况的时候进行唤醒 notify
    public synchronized void put(T t) throws InterruptedException {
        while(isFull()){
            wait();
        }
        doPut(t);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        T t = doTake();
        notifyAll();
        return t;
    }

    public synchronized void putFast(T t) throws InterruptedException {
        while(isFull()){
            wait();
        }
        doPut(t);
        boolean isEmpty = isEmpty();
        //状态由 满 变为 空 需要进行线程唤醒
        if(isEmpty){
            notifyAll();
        }
    }

    public synchronized T takeFast() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        T t = doTake();
        boolean isFull = isFull();
        //状态由 空 变为 满 需要进行线程唤醒
        if(isFull){
            notifyAll();
        }
        return t;
    }
}
