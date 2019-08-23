package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 异步接口一般用于应用启动的时候，获取一些配置信息
 */
public class ZooKeeper_GetChildren_API_ASync_Usage implements Watcher {

    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk = null;

    public static void main(String[] args) {
        String path = "/zk-book3";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000, new ZooKeeper_GetChildren_API_ASync_Usage());
            countDownLatch.await();

            zk.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            zk.create(path+"/c1","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);

            /**
             * 异步回到过程，是在调用函数执行完毕以后，会进行回调。
             * 然后新注册的watcher，会在create node的时候 自动触发
             */
            zk.getChildren(path,true,new IChildren2Callback(), "This is context");

            Thread.sleep(3000);
            zk.create(path+"/c2","".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);

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
            if(Event.EventType.None == event.getType() && null==event.getPath()){
                System.out.println("create connection");
                countDownLatch.countDown();
            }
            else if(event.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("ReGetChild:"+zk.getChildren(event.getPath(),true));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class IChildren2Callback implements AsyncCallback.Children2Callback{

    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Get Children znode result: [response code: "+rc+", param path: "+path+", ctx: "+ctx+", children list: "+children+", stat: "+stat.toString());
    }
}
