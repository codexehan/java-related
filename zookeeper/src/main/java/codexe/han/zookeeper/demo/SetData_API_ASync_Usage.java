package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * data[] 使用该数据内容 覆盖节点现在的数据内容
 * version 并发控制 cas
 * cb 异步回调函数
 * ctx 用于传递上下文信息
 */
public class SetData_API_ASync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) {
        String path = "/zk-book";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new SetData_API_ASync_Usage());
            countDownLatch.await();

            zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            zk.setData(path,"456".getBytes(),-1,new IStatCallback(), null);

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
        if(Event.KeeperState.SyncConnected == event.getState()){
            if(Event.EventType.None == event.getType() && null == event.getPath()){
                countDownLatch.countDown();
            }
        }
    }
}

class IStatCallback implements AsyncCallback.StatCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if(rc==0){
            System.out.println("Success");
        }
    }
}
