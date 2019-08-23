package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * create zk node by sync api
 */
public class ZooKeeper_Create_API_Sync_Usage implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {
        ZooKeeper zookeeper = null;
        try {
            zookeeper = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new ZooKeeper_Create_API_Sync_Usage());
            connectedSemaphore.await();
            String path1 = zookeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);//临时节点
            System.out.println("Success create znode: "+path1);

            String path2 = zookeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);//临时顺序节点
            System.out.println("Success create znode: "+path2);
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
        if(Event.KeeperState.SyncConnected == event.getState()){
            System.out.println("连接建立成功");
            connectedSemaphore.countDown();
        }
    }
}
