package codexe.han.cache.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

@Slf4j
public class TestRedis {
    public static void main(String[] args) {
        /*JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("localhost");
        jedisConnectionFactory.setPort(6379);
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);

        String value = String.valueOf(template.getConnectionFactory().getConnection().get("1".getBytes()));
        System.out.println(value);*/

        Jedis jedis = new Jedis("localhost");
//        jedis.set("1","test1");
  //      System.out.println(jedis.get("1"));

   //     System.out.println(jedis.keys("*"));
    //    System.out.println(jedis.hgetAll("Student:1"));

   //     StringOperation(jedis);
   //     ListOperation(jedis);
        SetOperation(jedis);
    }
    public static void StringOperation(Jedis jedis){

        log.info(jedis.get("testStrKey"));
        jedis.incr("testStrKey");
        log.info(jedis.get("testStrKey"));//自增++

        jedis.incrBy("testStrKey", 35);

  //      jedis.append("new-string-key", "hello ");
  //      jedis.append("new-string-key", " world!");

        log.info(jedis.substr("new-string-key",3,7));

        jedis.setrange("new-string-key",6,"W");//将从offset开始的子串佘紫薇给定值
        log.info(jedis.get("new-string-key"));

        jedis.setrange("new-string-key",6,"WW");//将从offset开始的子串佘紫薇给定值
        log.info(jedis.get("new-string-key"));
        jedis.setrange("new-string-key",40,"WW");//offset大于实际长度，将自动补空值
        log.info(jedis.get("new-string-key"));

        log.info(jedis.substr("new-string-key", 2,5));
        log.info(jedis.getrange("new-string-key",2,5));

    }

    public static void ListOperation(Jedis jedis){
     //   jedis.rpush("list-key","last");
     //   jedis.lpush("list-key","first");

    //    jedis.lpop("list-key");
        log.info("redis list is {}",jedis.lrange("list-key",0,-1));

        //同时写入多个元素
    //    jedis.rpush("list-key","1","2","3");

        //批量remove
        jedis.ltrim("list-key",2,-1);//只保留index 2以后的数字
        log.info("redis list is {}",jedis.lrange("list-key",0,-1));

        //还有可阻塞的brpop blpop brpoplpush(从一个list里面弹出，存到另外一个list中) 可以用作消息传递和任务队列



    }

    public static void SetOperation(Jedis jedis){
        jedis.sadd("set-key","a","b","c");
        jedis.srem("set-key","a");
        log.info(""+jedis.scard("set-key"));
        log.info(""+jedis.smembers("set-key"));
        jedis.smove("set-key","set-key2","c");//如果set1包含c 将c移到set2
        log.info(""+jedis.smembers("set-key2"));

        //集合高级运算
        //差运算
        log.info("1 2 差集是 {}",jedis.sdiff("set-key","set-key2"));
        //jedis.sdiffstore()

        //交集运算
        log.info("1 2 交集是 {}",jedis.sinter("set-key","set-key2"));
        //jedis.sinterstore()

        //并集运算
        log.info("1 2 并集是 {}",jedis.sunion("set-key","set-key2"));
        //jedis.sunionstore()
    }

    public static void HashOperation(Jedis jedis){
        /**
         批量获取 批量写入
         jedis.hmget()
         jedis.hmset()

         jedis.hincrby("hash-key", "key-name")//+1
         */
    }

    public static void ZSetOperation(Jedis jedis){

        /**
         jedis.zadd()
         jedis.zcard() 获取集合大小
         zincrby  分值加1
         zcount 返回分数介于min max之间的数值
         zrank 返回排名
         zscore 返回分数
         zrange 返回排名介于start和stop之间的member

         zrevrank 从大到小排名
         zrevrange 排名范围，从大到小返回
         zrangebyscore  按照分数排序，取某个分数区间的成员
         zremrangebyrank
         zremrangebyscore


         zinterstore
         zuninstore
         可以传入聚合函数，按照min,sum,max来进行选择
         */
    }
}
