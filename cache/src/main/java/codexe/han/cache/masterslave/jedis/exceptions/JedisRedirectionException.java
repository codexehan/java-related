package codexe.han.cache.masterslave.jedis.exceptions;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.exceptions.JedisDataException;

public class JedisRedirectionException extends JedisDataException {
  private static final long serialVersionUID = 3878126572474819403L;

  private HostAndPort targetNode;
  private int slot;

  public JedisRedirectionException(String message, HostAndPort targetNode, int slot) {
    super(message);
    this.targetNode = targetNode;
    this.slot = slot;
  }

  public JedisRedirectionException(Throwable cause, HostAndPort targetNode, int slot) {
    super(cause);
    this.targetNode = targetNode;
    this.slot = slot;
  }

  public JedisRedirectionException(String message, Throwable cause, HostAndPort targetNode, int slot) {
    super(message, cause);
    this.targetNode = targetNode;
    this.slot = slot;
  }

  public HostAndPort getTargetNode() {
    return targetNode;
  }

  public int getSlot() {
    return slot;
  }
}
