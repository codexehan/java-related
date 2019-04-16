package codexe.han.concurrency.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * 有一个任务A要等待其他四个任务执行完毕才能执行，就需要使用CountDownLatch
 * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
 *
 * 　　　　而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
 *
 * 　　　　另外，CountDownLatch是不能够重用的，而CyclicBarrier是可以重用的。
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(2);
        Thread thread1 = new Thread(()->{
            System.out.println("thread1 run");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });

        Thread thread2 = new Thread(()->{
            System.out.println("thread2 run");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });

        thread1.start();
        thread2.start();

        System.out.println("main thread waiting other threads one");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main thread done");
    }
}

class CyclicBarrierTest{
    public static void main(String[] args) {
        int N = 4;
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(N);
        for(int i=0; i<N;i++){
            new Thread(()->{
                try {
                    Thread.sleep(5000);
                    System.out.println(Thread.currentThread().getName()+" 写入数据完毕，等待其他线程...");
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("所有线程写入完毕，处理其他任务");
            }).start();
        }
    }

}
//CountDownLatch A线程等待其他所有线程执行完毕，然后执行
//CyclicBarrier 一组线程相互等待到达某个时候，然后各自执行
//Semaphore 控制同时访问的线程个数

class SemaphoreTest{
    public static void main(String[] args) {
        int workerNum = 8;
        final Semaphore semp = new Semaphore(5);
        for(int i=0; i<workerNum; i++){
            new Worker(semp, i).start();
        }
    }
    static class Worker extends Thread{
        private Semaphore semp;
        private int workerId;
        public Worker(Semaphore semp, int workerId){
            this.semp = semp;
            this.workerId = workerId;
        }
        @Override
        public void run(){
            try {
                semp.acquire();
                System.out.println("worker "+workerId+" 获取锁");
                Thread.sleep(2000);
                System.out.println("worker "+workerId+" 释放锁");
                semp.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
