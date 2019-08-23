package codexe.han.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;


public class Del_Data_Sample {
    static String path = "/zk-book/c1";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path,"init".getBytes());
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);//获取版本号
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);//执行删除操作


        /**
         * guaranteed接口是一个保障措施，只要客户端会话有效，那么curator会在后台持续进行删除操作，直到节点删除成功。
         */
        client.delete().guaranteed().forPath(path);
    }
}
