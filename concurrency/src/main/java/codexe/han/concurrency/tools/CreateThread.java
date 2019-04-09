package codexe.han.concurrency.tools;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CreateThread {
    public static void main(String[] args) {
        /**
         Thread Status

         NEW(os) — a new Thread instance that was not yet started via Thread.start()
         RUNNABLE(os) — a running thread. It is called runnable because at any given time it could be either running or waiting for the next quantum of time from the thread scheduler. A NEW thread enters the RUNNABLE state when you call Thread.start() on it
         RUNNING(os)
         BLOCKED(os) — a running thread becomes blocked if it needs to enter a synchronized section but cannot do that due to another thread holding the monitor of this section
         WAITING — a thread enters this state if it waits for another thread to perform a particular action. For instance, a thread enters this state upon calling the Object.wait() method on a monitor it holds, or the Thread.join() method on another thread
         TIMED_WAITING — same as the above, but a thread enters this state after calling timed versions of Thread.sleep(), Object.wait(), Thread.join() and some other methods
         TERMINATED(os) — a thread has completed the execution of its Runnable.run() method and terminated


         NEW - 刚创建，为调用start
         RUNNABLE - 调用了start
         RUNNING - 调用了run
         BLOCKED - 进入synchronized 区域
         WAITING - 调用 object.wait Thread.join
         TIMED-WAITING - object.wait(Long time, TimeUnit) thread.sleep() thread.join()
         TERMINATED - run()结束 或者调用terminated
         */
       /* MyThread myThread = new MyThread();
        myThread.start();//start 是一个native方法 将会调用run方法

        Thread myThread2 = new Thread(new MyThread2());
        myThread2.start();*/

/*
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futureList = new ArrayList<>();
        for(int i=0; i<5; i++){
            Future<Integer> future = fixedThreadPool.submit(new MyThread3(i));
            futureList.add(future);
        }

  //      fixedThreadPool.shutdown();//等待queue中的所有线程执行完毕在关闭
        for(Future<Integer> future : futureList){
            try {
                System.out.println(future.get(500, TimeUnit.MILLISECONDS));
            } catch (InterruptedException e) {
               System.out.println("thread is interrupted");
            } catch (ExecutionException e) {
                System.out.println("thread has execution exception");
                e.printStackTrace();
            } catch (TimeoutException e) {
                System.out.println("thread get result timeout");
                e.printStackTrace();
            }
        }

        fixedThreadPool.shutdown();//等待queue中的所有线程执行完毕在关闭
*/

        //ExecutorService submit和execute的区别在于，submit可以用来捕获异常，而execute执行的线程异常无法被主线程捕获
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for(int i=0;i<5; i++){
            Future future = cachedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    int i=0;
                    int a = 123/i;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " thread start");
                }
            });

            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("has execution exception "+e);

                e.printStackTrace();
            }
        }

        cachedThreadPool.shutdown();

        System.out.println("parent thread is done...");
    }
}

class MyThread extends Thread{
    @Override
    public void run(){
        System.out.println("MyThread start...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread name is "+ Thread.currentThread().getName());
        System.out.println("thread status is "+Thread.currentThread().getState());
    }
}

class MyThread2 implements Runnable{

    @Override
    public void run() {
        System.out.println("MyThread2 start...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread name is "+ Thread.currentThread().getName());
        System.out.println("thread status is "+Thread.currentThread().getState());
    }
}

class MyThread3 implements Callable<Integer>{

    private Integer a;
    public MyThread3(Integer a){
        this.a = a;
    }
    @Override
    public Integer call() throws Exception {
        System.out.println("callable thread "+a+" start...");
        Thread.sleep(1000);
        return ++a;//return a++;先返回a,再做++
    }
}

