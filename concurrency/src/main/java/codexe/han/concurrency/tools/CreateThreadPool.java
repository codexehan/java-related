package codexe.han.concurrency.tools;

import java.util.concurrent.*;

public class CreateThreadPool {
    public static void main(String[] args) {
        /**
         *
         */


        /**
         * Cached Thread Pool
         */
        //对于执行生命周期短的任务，性能很高
        //调用execute或者submit可以重用已经创建的thread
        //对于没有使用的线程，会在默认60s中丢失 keep alive time
        //core pool size为0 max size为Integer.max
        //不会存放到队列里面，每来一个任务，就会创建一个新的线程执行（如果之前创建的线程，处于空闲状态，会复用）
        // 有一个问题就是，并发太高的话，会出现线程过多的问题，
        ExecutorService executorService = Executors.newCachedThreadPool();
       /* for(int i=0;i<5;i++) {
            executorService.execute(() -> System.out.println("Current thread is " + Thread.currentThread().getName()));
        }
        for(int i=0;i<50;i++) {
            executorService.execute(() -> System.out.println("Current thread is " + Thread.currentThread().getName()));
        }*/

        for(int i=0;i<5;i++) {
            executorService.submit(() -> System.out.println("Current thread is " + Thread.currentThread().getName()));
        }
        for(int i=0;i<50;i++) {
            executorService.submit(() -> System.out.println("Current thread is " + Thread.currentThread().getName()));
        }
        executorService.shutdown();

        /**
         * Fixed Thread Pool
         */
        //core pool size 为n, max size为n
        //线程会一直存活 keep alive time 为0
        //无界队列存放暂时无法执行的任务
        //一个线程失败 会立刻创建另外一个线程替代
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for(int i=0;i<5;i++) {
            fixedThreadPool.submit(() -> System.out.println("Fixed thread pool current thread is " + Thread.currentThread().getName()));
        }
        fixedThreadPool.shutdown();

        /**
         *Scheduled Thread Pool
         */
        //周期性执行的线程是同一个线程
        //core pool size 为n
        Executors.newScheduledThreadPool(3);
        ScheduledExecutorService scheduledThreadPool= Executors.newScheduledThreadPool(3);
        scheduledThreadPool.schedule(()-> System.out.println("延迟三秒执行 "+Thread.currentThread().getName()), 3, TimeUnit.SECONDS);
        scheduledThreadPool.scheduleAtFixedRate(()->System.out.println("延迟一秒 每两秒执行一次 "+Thread.currentThread().getName()), 1, 2, TimeUnit.SECONDS);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduledThreadPool.shutdown();

        /**
         * Single Thread Pool
         */
        //core pool size is 1 max size is 1
        //keep alive time is 0
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.shutdown();

    }
}
