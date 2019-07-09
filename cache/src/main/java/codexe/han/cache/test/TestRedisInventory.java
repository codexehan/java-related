package codexe.han.cache.test;

import redis.clients.jedis.Jedis;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestRedisInventory {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("172.28.2.22",6379);

        String key = "product:inventory:1";
        System.out.println(jedis.set(key,"10"));

        int n=20;
        CyclicBarrier barrier = new CyclicBarrier(n);
        for(int i=0;i<n;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" ready");
                try {
                    barrier.await();
                    long remain = jedis.incrBy(key,-1);
                    System.out.println("remain "+remain);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
