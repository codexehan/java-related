package codexe.han.concurrency.tools;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        boolean wasEmpty = isEmpty();
        doPut(t);
        System.out.println("put "+t);
        //状态由 空 变为 非空 需要进行线程唤醒
        //主要是告知take操作可以进行take
        //conditional notify
        if(wasEmpty){
            notifyAll();
        }
    }

    public synchronized T takeFast() throws InterruptedException {
        while(isEmpty()){
            wait();
        }
        boolean wasFull = isFull();
        T t = doTake();
        System.out.println("take "+t);
        //状态由 满 变为 非满 需要进行线程唤醒
        //conditional notify
        //主要是告知put操作可以进行put
        if(wasFull){
            notifyAll();
        }
        return t;
    }

    public static void main(String[] args) {
        BufferWaitNotify<Integer> bufferWaitNotify = new BufferWaitNotify(3);
        Thread producer1 = new Thread(()->{
            for(int i =0; i< 5; i++) {
                try {
                    bufferWaitNotify.putFast(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread producer2 = new Thread(()->{
            for(int i =5; i< 10; i++) {
                try {
                    bufferWaitNotify.putFast(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        Thread consumer1 = new Thread(()->{
           while(!Thread.currentThread().isInterrupted()){
               try {
                   int i = bufferWaitNotify.takeFast();
               } catch (InterruptedException e) {
                   e.printStackTrace();
                   break;
               }
           }
        });
        Thread consumer2 = new Thread(()->{
           while(!Thread.currentThread().isInterrupted()){
               try {
                   int i = bufferWaitNotify.takeFast();
               } catch (InterruptedException e) {
                   e.printStackTrace();
                   break;
               }
           }
        });
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer1.interrupt();
        consumer2.interrupt();
    }
}

class BufferLockCondition<T> extends BaseBuffer<T>{

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BufferLockCondition(int capacity) {
        super(capacity);
    }

    public void put(T t) throws InterruptedException {
        lock.lock();//get lock
        try {
            while (isFull()) {
                notFull.await();
            }
            boolean wasEmpty = isEmpty();
            doPut(t);
            System.out.println("put "+t);
            if(wasEmpty){
                notEmpty.signalAll();
            }
        }finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try{
            while(isEmpty()){
                notEmpty.await();
            }
            boolean wasFull = isFull();
            T t = doTake();
            System.out.println("take "+t);
            if(wasFull){
                notFull.signalAll();
            }
            return t;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BufferLockCondition<Integer> bufferLockCondition = new BufferLockCondition<Integer>(3);
        Thread producer1 = new Thread(()->{
            for(int i =0; i< 5; i++) {
                try {
                    bufferLockCondition.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread producer2 = new Thread(()->{
            for(int i =5; i< 10; i++) {
                try {
                    bufferLockCondition.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        Thread consumer1 = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    int i = bufferLockCondition.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        Thread consumer2 = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    int i = bufferLockCondition.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer1.interrupt();
        consumer2.interrupt();
    }
}

class BaseBufferLockCondition<T>{
    private final T[] buffer;

    private int count;

    public BaseBufferLockCondition(int capacity){
        buffer = (T[])(new Object[capacity]);
    }
    private Lock lock = new ReentrantLock();
    private Condition notFull = lock.newCondition();
    private Condition notEmpty = lock.newCondition();

    public int head;
    private int tail;

    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            while (isFull()) {
                notFull.await();//在not full 条件队列上等待 condition queue
            }
            boolean wasEmpty = isEmpty();//空
            buffer[tail] = t;//非空
            System.out.println("put "+t);
            count++;
            if (++tail == buffer.length) {
                tail = 0;
            }
            if (wasEmpty) {//conditional signal
                notEmpty.signalAll();
            }
       //     notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try{
            while(isEmpty()){
                notEmpty.await();//在 not empty 条件队列上等待 condition queue
            }
            boolean wasFull = isFull();//满
            T t = buffer[head];//非满
            System.out.println("take "+t);
            buffer[head] = null;
            count--;
            if(++head==buffer.length){
                head = 0;
            }
            if(wasFull){
                notFull.signalAll();
            }
      //      notFull.signal();
            return t;
        } finally{
            lock.unlock();
        }
    }

    private boolean isFull(){
        return count == buffer.length;
    }
    private boolean isEmpty(){
        return count == 0;
    }

    public static void main(String[] args) {
        BaseBufferLockCondition<Integer> bufferLockCondition = new BaseBufferLockCondition<Integer>(8);
        Thread producer1 = new Thread(()->{
            for(int i =0; i< 20; i++) {
                try {
                    bufferLockCondition.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread producer2 = new Thread(()->{
            for(int i =20; i< 40; i++) {
                try {
                    bufferLockCondition.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });


        Thread consumer1 = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    int i = bufferLockCondition.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        Thread consumer2 = new Thread(()->{
            while(!Thread.currentThread().isInterrupted()){
                try {
                    int i = bufferLockCondition.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer1.interrupt();
        consumer2.interrupt();
    }
}
