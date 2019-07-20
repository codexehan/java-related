package codexe.han.cache.test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j

/**
 * 使用存储时间戳的模式，可以保证redis更新的顺序，从而保证redis和数据库的一致性。
 */
public class TestRedisTransaction {

    private static String address = "172.28.2.22";
    private static Integer port = 6379;

    public static void main(String[] args) {
        /*Jedis jedis = new Jedis(address, port);

        jedis.set("inventory:1","2");
        jedis.set("inventoryupdate:1","0");*/


  //      multiUpdate();
   //     testWatchNonExistsKey();
//        del();
//        multiUpdate();

             get();

    }
    public static void get(){
        Jedis jedis = new Jedis(address, port);

        System.out.println(jedis.get("inventory:1"));
        System.out.println(jedis.get("inventoryupdate:1"));
    }
    public static void del(){
        Jedis jedis = new Jedis(address, port);

        System.out.println(jedis.del("inventory:1"));
        System.out.println(jedis.del("inventoryupdate:1"));
    }
    public static void multiUpdate(){
        Jedis jedis = new Jedis(address, port);
        int n=1500;
        CyclicBarrier barrier = new CyclicBarrier(n);
        for(int i=1;i<=n;i++){
            UpdateThread thread = new UpdateThread();
            thread.setN(jedis,barrier, i);
            new Thread(thread).start();
        }
    }
    static class UpdateThread implements Runnable{
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
                    boolean exit = false;
                    Jedis jedis = new Jedis(address,port);
                    barrier.await();
                    while(!exit) {
                        try {
                            log.info("{} 还在竞争...", Thread.currentThread().getName());
                            jedis.watch("inventory:1", "inventoryupdate:1");
                            String updateTime = jedis.get("inventoryupdate:1");
                            int prev = Integer.parseInt(updateTime==null?"0":updateTime);
                            if (updateTime==null||prev < n) {
                                Transaction tx = jedis.multi();
                                tx.set("inventory:1", n.toString());
                                tx.set("inventoryupdate:1", n.toString());
                                List<Object> txRes = tx.exec();
                                if (txRes == null) {
                                    log.info("{} 被其他线程打断", Thread.currentThread().getName());
                                    continue;
                                } else {
                                    log.info("{} 更新完毕 {}, {}", Thread.currentThread().getName(), n, n);
                                    exit = true;
                                }
                            } else {//不是最新的数据 退出
                                jedis.unwatch();
                                log.info("{} 已经是旧数据，退出竞争行列", Thread.currentThread().getName());
                                exit = true;
                            }
                        }catch(JedisConnectionException e){
                        //    exit=true;
                            //产生exception就一直循环
                            log.info("连接用完异常",e);
                            log.info("{} 因为连接问题退出，请使用连接池！！！！",Thread.currentThread().getName());
                            exit=true;
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

    public static void testWatchNonExistsKey(){
        Jedis jedis = new Jedis(address);
        System.out.println(jedis.watch("abc123"));
        System.out.println(jedis.get("abc123"));
        System.out.println(jedis.unwatch());
    }
}
