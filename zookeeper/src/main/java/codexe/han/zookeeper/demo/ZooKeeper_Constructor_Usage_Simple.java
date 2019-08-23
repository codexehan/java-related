package codexe.han.zookeeper.demo;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * create zk session
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * connectString 集群中服务器地址+端口号
         * sessionTimeout 服务器和客户端之间会维护一个会话，在这个时间之内，会一直通过心跳维持，一旦超过这个时间，会话失效
         * sessionId和sessionPasswd代表会话ID和会话秘钥。这两个参数能够唯一确定一个会话，同时客户端使用这两个参数可以实现客户端会话复用，从而达到恢复会话的效果。
         * 定义好default watcher
         */


        ZooKeeper zooKeeper = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new ZooKeeper_Constructor_Usage_Simple());
        System.out.println("zk state is "+zooKeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zk session established");
        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();
        System.out.println("session id "+sessionId);
        System.out.println("passwd "+passwd);
        //use correct sessionId and sessionPassWd
        zooKeeper = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new ZooKeeper_Constructor_Usage_Simple(),sessionId,passwd);
        Thread.sleep(10000);

    }
    @Override
    public void process(WatchedEvent event) {
        System.out.println("Received warched event: "+event);
        if(Event.KeeperState.SyncConnected == event.getState()){
            connectedSemaphore.countDown();
        }
    }
}


