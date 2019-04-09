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

class B
