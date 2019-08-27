package codexe.han.zookeeper.curator.distributed_tool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Recipes_DistributedLock {
    static String lock_path = "/curator_recipes_lock_path";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    public static void main(String[] args) {
        client.start();

        final InterProcessMutex lock = new InterProcessMutex(client,lock_path);
        final CountDownLatch down = new CountDownLatch(1);
        for(int i=0;i<30;i++){
            new Thread(() -> {
                try{
                    down.await();
                    //阻塞式获取
                    lock.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                String orderNo = sdf.format(new Date());
                try {
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        down.countDown();
    }
}
