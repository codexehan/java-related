package codexe.han.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class GetData_API_ASync_Usage implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static ZooKeeper zk;

    public static void main(String[] args) {
        String path = "/zk-book5";
        try {
            zk = new ZooKeeper("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183",5000,new GetData_API_ASync_Usage());
            countDownLatch.await();

            zk.create(path,"123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

            zk.getData(path,true, new IDataCallback(), null);
            zk.setData(path,"123".getBytes(),-1);

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
                    zk.getData(event.getPath(), true, new IDataCallback(),null);
                }catch(Exception e){

                }
            }
        }
    }
}

class IDataCallback implements AsyncCallback.DataCallback{

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        System.out.println(rc+", "+path+" ,"+new String(data));
        System.out.println(stat.getCzxid()+","+stat.getMzxid()+","+stat.getVersion());
    }
}
