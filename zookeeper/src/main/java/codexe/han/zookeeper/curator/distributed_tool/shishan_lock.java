package codexe.han.zookeeper.curator.distributed_tool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 客户端A先发起一个加锁请求，创建一个临时节点，0000000001，确认自己是第一个子节点，获得锁，执行任务，删除0000000001节点，释放锁
 * 客户端B发起加锁请求，创建一个临时节点，0000000002，并确认自己是不是第一个，不是的话，监听0000000001
 *
 * 源码解析
 * internalLockLoop
 * while循环 自旋直至获得锁，或者到达超时时间
 * while循环内部
 * 1.获取所有子节点，并按从小到大顺序排序 getSortedChildren
 * 2.与最小节点比较，如果自己是最小节点，就获取锁，否则，监听比自己小一点的那个节点
 * 3.如果超时(wait(timeout))，则删除自己创建的节点 或者无限等待(wait())，dataWatcher 删除和并更都会通知到改事件 执行内容是notifyAll()唤醒线程。
 *
 * 当前一个节点被删除的时候，我们正好有线程对这个节点添加watcher， 就会抛出KeeperException.NoNodeException，然后重新执行while循环
 *
 */
public class shishan_lock {
    static String path = "/zk-lock6";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(50000)//default is 1min
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) {
        client.start();
        InterProcessMutex lock = new InterProcessMutex(client,path);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for(int i=0;i<2;i++) {
            new GetLock(countDownLatch,i,lock).start();
        }
        countDownLatch.countDown();
        client.close();

    }
}

class GetLock extends Thread{
    private int i=0;
    private CountDownLatch countDownLatch;
    private InterProcessMutex lock;
    public GetLock(CountDownLatch countDownLatch, int i, InterProcessMutex lock){
        this.i = i;
        this.countDownLatch = countDownLatch;
        this.lock = lock;
    }
    @Override
    public void run(){

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            boolean acquireRes = lock.acquire(100, TimeUnit.MILLISECONDS);
            System.out.println(Thread.currentThread().getName() + " get lock res: " + acquireRes);
            if (!acquireRes) {
                return;
            }
            else{
                int sleepTime =i==2?200:0;
                System.out.println(Thread.currentThread().getName() + " sleep time "+sleepTime);
            //    Thread.sleep(sleepTime);
            }
            lock.release();
            System.out.println(Thread.currentThread().getName() + " release lock ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
