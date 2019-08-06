package codexe.han.cache.masterslave.jedis;

import redis.clients.jedis.Client;

public abstract class JedisMonitor {
  protected redis.clients.jedis.Client client;

  public void proceed(Client client) {
    this.client = client;
    this.client.setTimeoutInfinite();
    do {
      String command = client.getBulkReply();
      onCommand(command);
    } while (client.isConnected());
  }

  public abstract void onCommand(String command);
}