package codexe.han.cache.masterslave.jedis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisFactory;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class JedisSentinelPool extends JedisPoolAbstract {

  protected GenericObjectPoolConfig poolConfig;

  protected int connectionTimeout = redis.clients.jedis.Protocol.DEFAULT_TIMEOUT;
  protected int soTimeout = redis.clients.jedis.Protocol.DEFAULT_TIMEOUT;

  protected String password;

  protected int database = redis.clients.jedis.Protocol.DEFAULT_DATABASE;

  protected String clientName;

  protected Set<MasterListener> masterListeners = new HashSet<MasterListener>();

  protected Logger log = LoggerFactory.getLogger(getClass().getName());

  private volatile redis.clients.jedis.JedisFactory factory;
  private volatile redis.clients.jedis.HostAndPort currentHostMaster;
  
  private final Object initPoolLock = new Object();

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig) {
    this(masterName, sentinels, poolConfig, redis.clients.jedis.Protocol.DEFAULT_TIMEOUT, null,
        redis.clients.jedis.Protocol.DEFAULT_DATABASE);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels) {
    this(masterName, sentinels, new GenericObjectPoolConfig(), redis.clients.jedis.Protocol.DEFAULT_TIMEOUT, null,
        redis.clients.jedis.Protocol.DEFAULT_DATABASE);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels, String password) {
    this(masterName, sentinels, new GenericObjectPoolConfig(), redis.clients.jedis.Protocol.DEFAULT_TIMEOUT, password);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password) {
    this(masterName, sentinels, poolConfig, timeout, password, redis.clients.jedis.Protocol.DEFAULT_DATABASE);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout) {
    this(masterName, sentinels, poolConfig, timeout, null, redis.clients.jedis.Protocol.DEFAULT_DATABASE);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final String password) {
    this(masterName, sentinels, poolConfig, Protocol.DEFAULT_TIMEOUT, password);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database) {
    this(masterName, sentinels, poolConfig, timeout, timeout, password, database);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, int timeout, final String password,
      final int database, final String clientName) {
    this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int timeout, final int soTimeout,
      final String password, final int database) {
    this(masterName, sentinels, poolConfig, timeout, soTimeout, password, database, null);
  }

  public JedisSentinelPool(String masterName, Set<String> sentinels,
      final GenericObjectPoolConfig poolConfig, final int connectionTimeout, final int soTimeout,
      final String password, final int database, final String clientName) {
    this.poolConfig = poolConfig;
    this.connectionTimeout = connectionTimeout;
    this.soTimeout = soTimeout;
    this.password = password;
    this.database = database;
    this.clientName = clientName;

    redis.clients.jedis.HostAndPort master = initSentinels(sentinels, masterName);
    initPool(master);
  }

  @Override
  public void destroy() {
    for (MasterListener m : masterListeners) {
      m.shutdown();
    }

    super.destroy();
  }

  public redis.clients.jedis.HostAndPort getCurrentHostMaster() {
    return currentHostMaster;
  }

  private void initPool(redis.clients.jedis.HostAndPort master) {
    synchronized(initPoolLock){
      if (!master.equals(currentHostMaster)) {
        currentHostMaster = master;
        if (factory == null) {
          factory = new redis.clients.jedis.JedisFactory(master.getHost(), master.getPort(), connectionTimeout,
              soTimeout, password, database, clientName);
          initPool(poolConfig, factory);
        } else {
          factory.setHostAndPort(currentHostMaster);
          // although we clear the pool, we still have to check the
          // returned object
          // in getResource, this call only clears idle instances, not
          // borrowed instances
          internalPool.clear();
        }

        log.info("Created JedisPool to master at " + master);
      }
    }
  }

  private redis.clients.jedis.HostAndPort initSentinels(Set<String> sentinels, final String masterName) {

    redis.clients.jedis.HostAndPort master = null;
    boolean sentinelAvailable = false;

    log.info("Trying to find master from available Sentinels...");

    for (String sentinel : sentinels) {
      final redis.clients.jedis.HostAndPort hap = redis.clients.jedis.HostAndPort.parseString(sentinel);

      log.debug("Connecting to Sentinel {}", hap);

      redis.clients.jedis.Jedis jedis = null;
      try {
        jedis = new redis.clients.jedis.Jedis(hap);

        List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);

        // connected to sentinel...
        sentinelAvailable = true;

        if (masterAddr == null || masterAddr.size() != 2) {
          log.warn("Can not get master addr, master name: {}. Sentinel: {}", masterName, hap);
          continue;
        }

        master = toHostAndPort(masterAddr);
        log.debug("Found Redis master at {}", master);
        break;
      } catch (JedisException e) {
        // resolves #1036, it should handle JedisException there's another chance
        // of raising JedisDataException
        log.warn(
          "Cannot get master address from sentinel running @ {}. Reason: {}. Trying next one.", hap,
          e.toString());
      } finally {
        if (jedis != null) {
          jedis.close();
        }
      }
    }

    if (master == null) {
      if (sentinelAvailable) {
        // can connect to sentinel, but master name seems to not
        // monitored
        throw new JedisException("Can connect to sentinel, but " + masterName
            + " seems to be not monitored...");
      } else {
        throw new JedisConnectionException("All sentinels down, cannot determine where is "
            + masterName + " master is running...");
      }
    }

    log.info("Redis master running at " + master + ", starting Sentinel listeners...");

    for (String sentinel : sentinels) {
      final redis.clients.jedis.HostAndPort hap = redis.clients.jedis.HostAndPort.parseString(sentinel);
      MasterListener masterListener = new MasterListener(masterName, hap.getHost(), hap.getPort());
      // whether MasterListener threads are alive or not, process can be stopped
      masterListener.setDaemon(true);
      masterListeners.add(masterListener);
      masterListener.start();
    }

    return master;
  }

  private redis.clients.jedis.HostAndPort toHostAndPort(List<String> getMasterAddrByNameResult) {
    String host = getMasterAddrByNameResult.get(0);
    int port = Integer.parseInt(getMasterAddrByNameResult.get(1));

    return new redis.clients.jedis.HostAndPort(host, port);
  }

  @Override
  public redis.clients.jedis.Jedis getResource() {
    while (true) {
      redis.clients.jedis.Jedis jedis = super.getResource();
      jedis.setDataSource(this);

      // get a reference because it can change concurrently
      final redis.clients.jedis.HostAndPort master = currentHostMaster;
      final redis.clients.jedis.HostAndPort connection = new HostAndPort(jedis.getClient().getHost(), jedis.getClient()
          .getPort());

      if (master.equals(connection)) {
        // connected to the correct master
        return jedis;
      } else {
        returnBrokenResource(jedis);
      }
    }
  }

  @Override
  protected void returnBrokenResource(final redis.clients.jedis.Jedis resource) {
    if (resource != null) {
      returnBrokenResourceObject(resource);
    }
  }

  @Override
  protected void returnResource(final redis.clients.jedis.Jedis resource) {
    if (resource != null) {
      resource.resetState();
      returnResourceObject(resource);
    }
  }

  protected class MasterListener extends Thread {

    protected String masterName;
    protected String host;
    protected int port;
    protected long subscribeRetryWaitTimeMillis = 5000;
    protected volatile redis.clients.jedis.Jedis j;
    protected AtomicBoolean running = new AtomicBoolean(false);

    protected MasterListener() {
    }

    public MasterListener(String masterName, String host, int port) {
      super(String.format("MasterListener-%s-[%s:%d]", masterName, host, port));
      this.masterName = masterName;
      this.host = host;
      this.port = port;
    }

    public MasterListener(String masterName, String host, int port,
        long subscribeRetryWaitTimeMillis) {
      this(masterName, host, port);
      this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
    }

    @Override
    public void run() {

      running.set(true);

      while (running.get()) {

        j = new Jedis(host, port);

        try {
          // double check that it is not being shutdown
          if (!running.get()) {
            break;
          }
          
          /*
           * Added code for active refresh
           */
          List<String> masterAddr = j.sentinelGetMasterAddrByName(masterName);  
          if (masterAddr == null || masterAddr.size() != 2) {
            log.warn("Can not get master addr, master name: {}. Sentinel: {}：{}.",masterName,host,port);
          }else{
              initPool(toHostAndPort(masterAddr)); 
          }

          j.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
              log.debug("Sentinel {}:{} published: {}.", host, port, message);

              String[] switchMasterMsg = message.split(" ");

              if (switchMasterMsg.length > 3) {

                if (masterName.equals(switchMasterMsg[0])) {
                  initPool(toHostAndPort(Arrays.asList(switchMasterMsg[3], switchMasterMsg[4])));
                } else {
                  log.debug(
                    "Ignoring message on +switch-master for master name {}, our master name is {}",
                    switchMasterMsg[0], masterName);
                }

              } else {
                log.error(
                  "Invalid message received on Sentinel {}:{} on channel +switch-master: {}", host,
                  port, message);
              }
            }
          }, "+switch-master");

        } catch (JedisException e) {

          if (running.get()) {
            log.error("Lost connection to Sentinel at {}:{}. Sleeping 5000ms and retrying.", host,
              port, e);
            try {
              Thread.sleep(subscribeRetryWaitTimeMillis);
            } catch (InterruptedException e1) {
              log.error("Sleep interrupted: ", e1);
            }
          } else {
            log.debug("Unsubscribing from Sentinel at {}:{}", host, port);
          }
        } finally {
          j.close();
        }
      }
    }

    public void shutdown() {
      try {
        log.debug("Shutting down listener on {}:{}", host, port);
        running.set(false);
        // This isn't good, the Jedis object is not thread safe
        if (j != null) {
          j.disconnect();
        }
      } catch (Exception e) {
        log.error("Caught exception while shutting down: ", e);
      }
    }
  }
}