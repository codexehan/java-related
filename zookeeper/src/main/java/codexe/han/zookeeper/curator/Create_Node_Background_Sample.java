package codexe.han.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 调用的时候，没有传入任何线程池，会使用zookeeper
 */
public class Create_Node_Background_Sample {
    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    static CountDownLatch countDownLatch = new CountDownLatch(2);

    static ExecutorService tp = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {
        client.start();
        System.out.println("Main thread: "+Thread.currentThread().getName());
        //此处传入了自定义的executor
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: "+event.getResultCode()+", type: "
                        +event.getType()+"]");
                        System.out.println("Thread of processResult: "+Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                },tp).forPath(path,"init".getBytes());
        //不包含线程池 使用event thread来处理  zl watcher event thread是单线程的
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        System.out.println("event[code: "+event.getResultCode()+", type: "
                        +event.getType()+"]");
                        System.out.println("Thread of processResult: "+Thread.currentThread().getName());
                        countDownLatch.countDown();
                    }
                }).forPath(path,"init".getBytes());

        countDownLatch.await();
        tp.shutdown();
    }
}
