package codexe.han.cache.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用redis实现分布式锁
 */
public class RedisDistributedLock {
    public static void main(String[] args) {

    }
    public String acquireLock(Jedis jedis, String lockName, int acquireTime, int lockTimeout){
        lockName = "lock:"+lockName;
        String val = UUID.randomUUID().toString();
        long acquireEnd = System.currentTimeMillis()+acquireTime;

        while(System.currentTimeMillis()<acquireEnd){
            if(jedis.setnx(lockName, val)==1){
                jedis.expire(lockName, lockTimeout);
                return val;
            }
        }
        return null;
    }

    public boolean releaseLock(Jedis jedis, String lockName, String val){
        while(true){
            jedis.watch(lockName);
            if(jedis.get(lockName).equals(val)){
                Transaction tx = jedis.multi();
                tx.del(lockName);
                List<Object> res=  tx.exec();
                if(!res.isEmpty()){
                    return true;
                }
            }
            else{
                jedis.unwatch();
                break;//锁已经被别人修改
            }
        }
        return false;
    }
}
