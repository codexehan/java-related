package codexe.han.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * retryPolicy 重试策略，主要有四种实现，分别是 ExponentialBackoffRetry, RetryNTimes, RetryOneTime, RetryUntilElapsed
 * sessionTimeoutMs 会话超时时间，默认是 60 000ms  如果在这个时间之内，client和客户端没有联系，那么session就会失效，然后对应的临时节点就会被删除
 * connectionTimeoutMs 连接创建超时时间，默认是15 000ms
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws InterruptedException {
        /**
         * retryCount 已经重试的次数，如果是第一次重试，那么参数为0
         * elapsedTimeMs 从第一次重试开始已经花费的时间，单位为毫秒
         * sleeper 用于sleep指定的时间，curator建议不适用Thread.sleep进行sleep操作
         */
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
    //    CuratorFramework client = CuratorFrameworkFactory.newClient("",5000,3000,retryPolicy);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();

        /**
         * 为了实现不同业务之间的隔离，可以为每个业务分配一个独立的命名空间
         */
        client = CuratorFrameworkFactory.builder()
                .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .namespace("base")//会话根目录是/base
                .build();
        client.start();
        Thread.sleep(5000);
    }
}
