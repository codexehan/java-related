package codexe.han.concurrency.tools;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ThreadLocal使用场景为 用来解决 数据库连接，session管理等
 * 每个线程有一个ThreadLocalMap对象
 * 调用ThreadLocal.get()就是首先调用getMap(t)返回t线程的LocalThreadMap => 当前线程.threadLocals.get(this->ThreadLocal)  没有值就返回initial value    //
 * 调用ThreadLocal.set(v) => Thread.currentThread().threadLocals.set(this->threadlocal, v);
 */
public class ThreadLocalTest {
    private static final ThreadLocal threadSession = new ThreadLocal();

    public static void main(String[] args) throws InterruptedException {
        /**
         * Thread t = Thread.currentThread();
         * // ThreadLocalMap getMap(Thread t) {
         * //        return t.threadLocals;
         * //    }
         *         ThreadLocalMap map = getMap(t);// ThreadLocal.ThreadLocalMap threadLocals = null;
         *         if (map != null) {
         *             ThreadLocalMap.Entry e = map.getEntry(this);
         *             if (e != null) {
         *                 @SuppressWarnings("unchecked")
         *                 T result = (T)e.value;
         *                 return result;
         *             }
         *         }
         *         return setInitialValue();
         */

        for(int i=0;i<5; i++){
            new ThreadLocalThread().start();
        }

        ReentrantLock lock = new ReentrantLock();
        lock.lock();//不可相应中断
        lock.lockInterruptibly();//可响应中断

    }
}

class ThreadLocalThread extends Thread{
    private final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(()->2);

    @Override
    public void run(){
        try {
            Thread.sleep(new Random().nextInt(10)*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+" thread local is "+threadLocal.get());
        threadLocal.set(6);
        System.out.println(Thread.currentThread().getName()+" thread local is "+threadLocal.get());
    }

}














