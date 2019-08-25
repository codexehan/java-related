package codexe.han.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * 只能对一级子节点监听，无法监听二级子节点
 */
public class PathChildrenCache_Sample {
    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString("")
            .retryPolicy(new ExponentialBackoffRetry(1000,3))
            .sessionTimeoutMs(5000)
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        /**
         * cachedata: true
         * 客户端收到变更消息后，会拉取相应的数据内容
         */
        PathChildrenCache cache = new PathChildrenCache(client,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch(event.getType()){
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED,"+event.getData().getPath());
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED,"+event.getData().getPath());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED,"+event.getData().getPath());
                        break;
                    default:
                        break;
                }
            }
        });

        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
        Thread.sleep(1000);

        client.create().withMode(CreateMode.PERSISTENT).forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path+"/c1");
        Thread.sleep(1000);
        client.delete().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
