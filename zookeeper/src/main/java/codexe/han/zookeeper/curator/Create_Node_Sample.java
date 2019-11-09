package codexe.han.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * Curator默认创建持久化节点
 *
 */
public class Create_Node_Sample {
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();
    public static void main(String[] args) throws Exception {
        ConnectionState.LOST;
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());

        /**
         * 创建一个临时节点，并自动递归的创建父节点
         * zk中所有父节点必须是持久节点
         */
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);// /zkbook 节点会被自动创建 持久节点
    }
}
