package codexe.han.zookeeper.curator.distributed_tool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 客户端A先发起一个加锁请求，创建一个临时节点，0000000001，确认自己是第一个子节点，获得锁，执行任务，删除0000000001节点，释放锁
 * 客户端B发起加锁请求，创建一个临时节点，0000000002，并确认自己是不是第一个，不是的话，监听0000000001
 */
public class shishan_lock {
    static String path = "/zk-lock";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(client,path);

        lock.acquire();

        lock.release();
    }
}
