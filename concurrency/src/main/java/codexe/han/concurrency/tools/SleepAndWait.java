package codexe.han.concurrency.tools;

public class SleepAndWait {
    public static void main(String[] args) {
        /**
         sleep 不会释放锁
         wait 会释放对象锁
         */
        LockObj lockObj = new LockObj();
        Thread thread1 = new Thread(()->{lockObj.run();});
        Thread thread2 = new Thread(()->{lockObj.run();});

        thread1.start();
        thread2.start();

    }
}
class LockObj {
    public void run(){
        synchronized (this){
            System.out.println(Thread.currentThread().getName()+"get lock");
            System.out.println(Thread.currentThread().getName()+"sleep 5s");
            try {
                Thread.sleep(5000);//sleep 2s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"wake");
            System.out.println(Thread.currentThread().getName()+"wait 5s");
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"exit");
        }
    }
}
