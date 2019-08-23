package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GetData_API_Sync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();

    public static void main(String[] args) {
        String path = "/zk-book4";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new GetData_API_Sync_Usage());
            countDownLatch.await();

            zk.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println(new String(zk.getData(path,true,stat)));

            System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());

            zk.setData(path,"123".getBytes(),-1);//version:-1 表示会匹配任何version  解释数据相同，版本号也会变化

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
                countDownLatch.countDown();
            }
            else if(event.getType() == Event.EventType.NodeDataChanged){
                try{
                    System.out.println(new String(zk.getData(event.getPath(),true,stat)));
                    System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
