package codexe.han.cache.test;

import redis.clients.jedis.Jedis;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class TestRedisInventory {
    public static void main(String[] args) {



        String key = "product:inventory:1";
   //     System.out.println(jedis.set(key,"10"));

        int n=20;
        CyclicBarrier barrier = new CyclicBarrier(n);
        for(int i=0;i<n;i++){
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" ready");
                try {
                    Jedis jedis = new Jedis("localhost",6379);
                    barrier.await();
                    long remain = jedis.decrBy(key,1);
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
