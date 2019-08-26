package codexe.han.zookeeper.curator.distributed_tool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 由主线程通过barrier.removeBarrier触发
 */
public class Recepes_Barrier {
    static String barrier_path = "/curator_recipes_barrier_path";

    static DistributedBarrier barrier;

    public static void main(String[] args) throws Exception {
        for(int i=0;i<5;i++){
            new Thread(()->{
                try{
                    CuratorFramework client = CuratorFrameworkFactory.builder()
                            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
                            .retryPolicy(new ExponentialBackoffRetry(1000,3))
                            .build();
                    barrier = new DistributedBarrier(client,barrier_path);
                    System.out.println(Thread.currentThread().getName());

                    barrier.setBarrier();
                    barrier.waitOnBarrier();;
                    System.out.println("启动...");
                }catch(Exception e){}
            }).start();
        }

        Thread.sleep(2000);
        barrier.removeBarrier();
    }
}

/**
 * 每个线程通过enter方法进入等待，此时处于准备进入状态
 * 一旦成员数量到达5个，所有成员会被同事触发
 */
class Recipes_Barrier2{
    static String barrier_path = "/curator_recipes_barrier_path";

    public static void main(String[] args) {
        for(int i=0;i<5;i++){
            new Thread(()->{
                try{
                    CuratorFramework client = CuratorFrameworkFactory.builder()
                            .connectString("172.28.2.19:2181,172.28.2.20:2182,172.28.2.24:2183")
                            .retryPolicy(new ExponentialBackoffRetry(1000,3))
                            .build();
                    client.start();
                    DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client,barrier_path,5);//当数目到达五个的时候，自动触发
                    Thread.sleep(Math.round(Math.random()*3000));

                    System.out.println(Thread.currentThread().getName()+"号进入barrier");
                    barrier.enter();
                    System.out.println("启动...");
                    Thread.sleep(Math.round(Math.random()*3000));

                    barrier.leave();
                    System.out.println("退出...");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
