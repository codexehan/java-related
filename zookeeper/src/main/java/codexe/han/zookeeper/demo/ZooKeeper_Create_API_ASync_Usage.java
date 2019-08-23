package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZooKeeper_Create_API_ASync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new ZooKeeper_Create_API_ASync_Usage());
            countDownLatch.await();

            zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(),"I am context.");
            zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,new IStringCallback(),"I am context.");
            zooKeeper.create("/zk-test-ephemeral-","".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,new IStringCallback(),"I am context.");

            Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void process(WatchedEvent event) {
        if(Event.KeeperState.SyncConnected == event.getState()){
            countDownLatch.countDown();
        }
    }
}

class IStringCallback implements AsyncCallback.StringCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        /**
         * 节点的创建过程是异步的（包括网络通信和服务端的节点创建过程）
         * 同步接口调用需要关注接口抛出异常
         * 但是在异步接口调用中，接口本身不会跑出异常，所有的异常都会在回调函数中通过Result Code（rc响应码）来体现
         * rc
         * 0 接口调用成功
         * -4 客户端和服务端连接已断开
         * -110 指定节点已存在
         * -112 会话已过期
         */
        System.out.println("create path result: ["+rc+", "+path+", "+ctx+", real path name: "+name);
    }
}
