package codexe.han.cache.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestRedisTransaction {
    public static void main(String[] args) {
     /*   Jedis jedis = new Jedis("localhost");

        jedis.set("inventory:1","2");
        jedis.set("inventoryupdate:1","0");*/

        multiUpdate();

   //     get();

    }
    public static void get(){
        Jedis jedis = new Jedis("localhost");

        System.out.println(jedis.get("inventory:1"));
        System.out.println(jedis.get("inventoryupdate:1"));
    }
    public static void multiUpdate(){
        Jedis jedis = new Jedis("localhost");
        int n=1;
        CyclicBarrier barrier = new CyclicBarrier(n);
        for(int i=1;i<=n;i++){
            UpdateThread thread = new UpdateThread();
            thread.setN(jedis,barrier, i);
            new Thread(thread).start();
        }

    }
}
class UpdateThread implements Runnable{
//https://groups.google.com/forum/#!topic/jedis_redis/jFngp7PTYrw
    private Integer n;
    private CyclicBarrier barrier;

    private Jedis jedis;

    public void setN(Jedis jedis, CyclicBarrier barrier, int n){
        this.barrier = barrier;
        this.n=n;
        this.jedis = jedis;
    }
    @Override
    public void run() {
        {
            System.out.println(Thread.currentThread().getName()+" ready");
            try {
                Jedis jedis = new Jedis("localhost",6379);
                boolean exit = false;
                barrier.await();
                while(!exit) {
                    try {
                        jedis.watch("inventory:1", "inventoryupdate:1");
                        int prev = Integer.parseInt(jedis.get("inventoryupdate:1"));
                        if (prev < n) {
                            Transaction tx = jedis.multi();
                            jedis.set("inventory:1", n.toString());
                            jedis.set("inventoryupdate:1", n.toString());
                            tx.exec();
                            exit = true;
                        }
                        else{//不是最新的数据 退出
                            jedis.unwatch();
                            exit=true;
                        }
                    }catch(Exception e){
                        exit=true;
                        //产生exception就一直循环
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
