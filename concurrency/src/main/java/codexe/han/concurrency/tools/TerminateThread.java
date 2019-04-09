package codexe.han.concurrency.tools;

public class TerminateThread {
    public static void main(String[] args) {
        /**
         volatile 终止线程可能会出现永远阻塞状态，例如生产者消费者模式下
         */
        Thread volatileThread = new VolatileThread();
        volatileThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((VolatileThread) volatileThread).exit();

        /**
         Interrupt 终止线程
         1.线程处于阻塞状态 try catch break 跳出
         2.线程处于非阻塞状态 isInterrupted跳出
         */
        Thread interruptThread = new InterruptThread();
        interruptThread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        interruptThread.interrupt();

        /**
         Thread.stop()强行终止线程 释放占有的锁 导致保护的数据出现不一致性
         */
    }
}
class VolatileThread extends Thread{
    private volatile boolean exit = false;//only use get and set

    public void run(){
        while(!exit){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread running...");
        }
        System.out.println("Thread exit...");
    }

    public void exit(){
        this.exit = true;
    }

}

class InterruptThread extends Thread{
    //interrupt try catch interrupt exception

    public void run(){
        while(!isInterrupted()){
            try{
                Thread.sleep(1000);
                System.out.println("Interrupt thread running");
            }catch (InterruptedException e) {
                e.printStackTrace();
                break;//阻塞状态下 接收中断 跳出循环
            }
        }
        System.out.println("Interrupt thread exit");
    }
}
