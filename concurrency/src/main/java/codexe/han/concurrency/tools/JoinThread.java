package codexe.han.concurrency.tools;

public class JoinThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+" 线程开始运行");
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(10000);
                System.out.println("t_1 结束运行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread.setName("t_1");
        thread.join();

        System.out.println("t_1结束，主线程继续运行");
    }
}
