package codexe.han.cache.masterslave.jedis;

import redis.clients.jedis.HostAndPort;

public interface JedisClusterHostAndPortMap {
  HostAndPort getSSLHostAndPort(String host, int port);
}
