package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * getChildren接口参数解析
 * path 获取该节点的子节点列表
 * watcher监听节点变更事件的watcher
 * warch true使用建立连接时候的默认watcher
 * cb 注册一个异步回掉函数
 * ctx 用于上下文传递的信息
 * stat 指定数据节点的节点状态信息。包括事务ID,最后一次袖肥的事务ID和数据节点内容的长度等。stat传入API接口，会在执行过程中，被来自服务端的相应的新stat对象替换。
 */
public class ZooKeeper_GetChildren_API_Sync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new ZooKeeper_GetChildren_API_Sync_Usage());
            countDownLatch.await();
            zk.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

            zk.create(path+"/c1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);

            List<String> childrenList = zk.getChildren(path,true);//重新注册一个新的watcher
            System.out.println("children list is "+childrenList);

            zk.create(path+"/c2","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            Thread.sleep(6000);

            zk.create(path+"/c3","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        /**
         * keeper state
         * event type
         * path
         */
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                System.out.println("create connection");
                countDownLatch.countDown();
            }
            else if(event.getType() == Event.EventType.NodeChildrenChanged){
                System.out.println("node children changed");
                try {
                    /**
                     * 1.sendThread获取服务端通知，解析event入队
                     * 2.eventthread执行对应时间，拉取节点信息
                     */
                    System.out.println("ReGetChildren:"+zk.getChildren(event.getPath(),true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
