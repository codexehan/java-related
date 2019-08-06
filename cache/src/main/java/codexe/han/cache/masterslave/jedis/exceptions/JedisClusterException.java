package codexe.han.cache.masterslave.jedis.exceptions;

import redis.clients.jedis.exceptions.JedisDataException;

public class JedisClusterException extends JedisDataException {
  private static final long serialVersionUID = 3878126572474819403L;

  public JedisClusterException(Throwable cause) {
    super(cause);
  }

  public JedisClusterException(String message, Throwable cause) {
    super(message, cause);
  }

  public JedisClusterException(String message) {
    super(message);
  }
}
