package codexe.han.cache.test;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

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
        jedis.set("1","test1");
        System.out.println(jedis.get("1"));
    }
}
