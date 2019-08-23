package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * watcher 用于监听 节点创建   节点删除   节点更新
 *
 * 无论节点是否存在，都可以通过exists注册watcher
 * 对于指定节点的子节点的各种变化，都不会通知客户端
 */
public class Exist_API_Sync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) {
        String path = "/zk-book7";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new Exist_API_Sync_Usage());

            countDownLatch.await();

            Thread.sleep(10000);
            System.out.println("验证5s后的session 过期以后, 是否可以继续进行操作");

            /**
             * !!!!! 节点尚未注册之前 就添加了exist watcher
             */
            zk.exists(path,true);
            zk.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            zk.setData(path,"123".getBytes(),-1);

            zk.create(path+"/c1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);

            zk.delete(path+"/c1",-1);
            zk.delete(path,-1);

            Thread.sleep(5000);
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
        try {
            if (Event.KeeperState.SyncConnected == event.getState()) {
                if (Event.EventType.None == event.getType() && null == event.getPath()) {
                    countDownLatch.countDown();
                } else if (Event.EventType.NodeCreated == event.getType()) {
                    System.out.println("Node(" + event.getPath() + ")Created");
                    zk.exists(event.getPath(), true);
                } else if(Event.EventType.NodeDeleted == event.getType()){
                    System.out.println("Node(" + event.getPath() + ")Deleted");
                    zk.exists(event.getPath(), true);
                } else if(Event.EventType.NodeDataChanged == event.getType()){
                    System.out.println("Node(" + event.getPath() + ")DataChanged");
                    zk.exists(event.getPath(), true);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
