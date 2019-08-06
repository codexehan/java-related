package codexe.han.cache.masterslave.jedis;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

public class JedisPoolAbstract extends Pool<redis.clients.jedis.Jedis> {

  public JedisPoolAbstract() {
    super();
  }

  public JedisPoolAbstract(GenericObjectPoolConfig poolConfig, PooledObjectFactory<redis.clients.jedis.Jedis> factory) {
    super(poolConfig, factory);
  }

  @Override
  protected void returnBrokenResource(redis.clients.jedis.Jedis resource) {
    super.returnBrokenResource(resource);
  }

  @Override
  protected void returnResource(Jedis resource) {
    super.returnResource(resource);
  }
}
