package codexe.han.cache.masterslave.jedis;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Pool;

public class ShardedJedisPool extends Pool<redis.clients.jedis.ShardedJedis> {
  public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<redis.clients.jedis.JedisShardInfo> shards) {
    this(poolConfig, shards, Hashing.MURMUR_HASH);
  }

  public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<redis.clients.jedis.JedisShardInfo> shards,
      Hashing algo) {
    this(poolConfig, shards, algo, null);
  }

  public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<redis.clients.jedis.JedisShardInfo> shards,
      Pattern keyTagPattern) {
    this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
  }

  public ShardedJedisPool(final GenericObjectPoolConfig poolConfig, List<redis.clients.jedis.JedisShardInfo> shards,
      Hashing algo, Pattern keyTagPattern) {
    super(poolConfig, new ShardedJedisFactory(shards, algo, keyTagPattern));
  }

  @Override
  public redis.clients.jedis.ShardedJedis getResource() {
    redis.clients.jedis.ShardedJedis jedis = super.getResource();
    jedis.setDataSource(this);
    return jedis;
  }

  @Override
  protected void returnBrokenResource(final redis.clients.jedis.ShardedJedis resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  @Override
  protected void returnResource(final redis.clients.jedis.ShardedJedis resource) {
    if (resource != null) {
      resource.resetState();
      returnResourceObject(resource);
    }
  }

  /**
   * PoolableObjectFactory custom impl.
   */
  private static class ShardedJedisFactory implements PooledObjectFactory<redis.clients.jedis.ShardedJedis> {
    private List<redis.clients.jedis.JedisShardInfo> shards;
    private Hashing algo;
    private Pattern keyTagPattern;

    public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
      this.shards = shards;
      this.algo = algo;
      this.keyTagPattern = keyTagPattern;
    }

    @Override
    public PooledObject<redis.clients.jedis.ShardedJedis> makeObject() throws Exception {
      redis.clients.jedis.ShardedJedis jedis = new redis.clients.jedis.ShardedJedis(shards, algo, keyTagPattern);
      return new DefaultPooledObject<redis.clients.jedis.ShardedJedis>(jedis);
    }

    @Override
    public void destroyObject(PooledObject<redis.clients.jedis.ShardedJedis> pooledShardedJedis) throws Exception {
      final redis.clients.jedis.ShardedJedis shardedJedis = pooledShardedJedis.getObject();
      for (redis.clients.jedis.Jedis jedis : shardedJedis.getAllShards()) {
        if (jedis.isConnected()) {
          try {
            try {
              jedis.quit();
            } catch (Exception e) {

            }
            jedis.disconnect();
          } catch (Exception e) {

          }
        }
      }
    }

    @Override
    public boolean validateObject(PooledObject<redis.clients.jedis.ShardedJedis> pooledShardedJedis) {
      try {
        redis.clients.jedis.ShardedJedis jedis = pooledShardedJedis.getObject();
        for (Jedis shard : jedis.getAllShards()) {
          if (!shard.ping().equals("PONG")) {
            return false;
          }
        }
        return true;
      } catch (Exception ex) {
        return false;
      }
    }

    @Override
    public void activateObject(PooledObject<redis.clients.jedis.ShardedJedis> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<ShardedJedis> p) throws Exception {

    }
  }
}