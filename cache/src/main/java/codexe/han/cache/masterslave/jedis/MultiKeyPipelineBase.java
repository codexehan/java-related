package codexe.han.cache.masterslave.jedis;

import redis.clients.jedis.*;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.commands.*;
import redis.clients.jedis.params.MigrateParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MultiKeyPipelineBase extends PipelineBase implements
        MultiKeyBinaryRedisPipeline, MultiKeyCommandsPipeline, ClusterPipeline,
    BinaryScriptingCommandsPipeline, ScriptingCommandsPipeline, BasicRedisPipeline {

  protected Client client = null;

  @Override
  public redis.clients.jedis.Response<List<String>> brpop(String... args) {
    client.brpop(args);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  public redis.clients.jedis.Response<List<String>> brpop(int timeout, String... keys) {
    client.brpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<List<String>> blpop(String... args) {
    client.blpop(args);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  public redis.clients.jedis.Response<List<String>> blpop(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  public redis.clients.jedis.Response<Map<String, String>> blpopMap(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_MAP);
  }

  @Override
  public redis.clients.jedis.Response<List<byte[]>> brpop(byte[]... args) {
    client.brpop(args);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_LIST);
  }

  public redis.clients.jedis.Response<List<String>> brpop(int timeout, byte[]... keys) {
    client.brpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  public redis.clients.jedis.Response<Map<String, String>> brpopMap(int timeout, String... keys) {
    client.blpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_MAP);
  }

  @Override
  public redis.clients.jedis.Response<List<byte[]>> blpop(byte[]... args) {
    client.blpop(args);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_LIST);
  }

  public redis.clients.jedis.Response<List<String>> blpop(int timeout, byte[]... keys) {
    client.blpop(timeout, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<Long> del(String... keys) {
    client.del(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> del(byte[]... keys) {
    client.del(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> unlink(String... keys) {
    client.unlink(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> unlink(byte[]... keys) {
    client.unlink(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> exists(String... keys) {
    client.exists(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> exists(byte[]... keys) {
    client.exists(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Set<String>> keys(String pattern) {
    getClient(pattern).keys(pattern);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_SET);
  }

  @Override
  public redis.clients.jedis.Response<Set<byte[]>> keys(byte[] pattern) {
    getClient(pattern).keys(pattern);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public redis.clients.jedis.Response<List<String>> mget(String... keys) {
    client.mget(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<List<byte[]>> mget(byte[]... keys) {
    client.mget(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_LIST);
  }

  @Override
  public redis.clients.jedis.Response<String> mset(String... keysvalues) {
    client.mset(keysvalues);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> mset(byte[]... keysvalues) {
    client.mset(keysvalues);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> msetnx(String... keysvalues) {
    client.msetnx(keysvalues);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> msetnx(byte[]... keysvalues) {
    client.msetnx(keysvalues);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> rename(String oldkey, String newkey) {
    client.rename(oldkey, newkey);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> rename(byte[] oldkey, byte[] newkey) {
    client.rename(oldkey, newkey);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> renamenx(String oldkey, String newkey) {
    client.renamenx(oldkey, newkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
    client.renamenx(oldkey, newkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> rpoplpush(String srckey, String dstkey) {
    client.rpoplpush(srckey, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
    client.rpoplpush(srckey, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public redis.clients.jedis.Response<Set<String>> sdiff(String... keys) {
    client.sdiff(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_SET);
  }

  @Override
  public redis.clients.jedis.Response<Set<byte[]>> sdiff(byte[]... keys) {
    client.sdiff(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public redis.clients.jedis.Response<Long> sdiffstore(String dstkey, String... keys) {
    client.sdiffstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
    client.sdiffstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Set<String>> sinter(String... keys) {
    client.sinter(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_SET);
  }

  @Override
  public redis.clients.jedis.Response<Set<byte[]>> sinter(byte[]... keys) {
    client.sinter(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public redis.clients.jedis.Response<Long> sinterstore(String dstkey, String... keys) {
    client.sinterstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
    client.sinterstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> smove(String srckey, String dstkey, String member) {
    client.smove(srckey, dstkey, member);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
    client.smove(srckey, dstkey, member);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sort(String key, redis.clients.jedis.SortingParams sortingParameters, String dstkey) {
    client.sort(key, sortingParameters, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
    client.sort(key, sortingParameters, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sort(String key, String dstkey) {
    client.sort(key, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sort(byte[] key, byte[] dstkey) {
    client.sort(key, dstkey);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Set<String>> sunion(String... keys) {
    client.sunion(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_SET);
  }

  @Override
  public redis.clients.jedis.Response<Set<byte[]>> sunion(byte[]... keys) {
    client.sunion(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY_ZSET);
  }

  @Override
  public redis.clients.jedis.Response<Long> sunionstore(String dstkey, String... keys) {
    client.sunionstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
    client.sunionstore(dstkey, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> watch(String... keys) {
    client.watch(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> watch(byte[]... keys) {
    client.watch(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> zinterstore(String dstkey, String... sets) {
    client.zinterstore(dstkey, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
    client.zinterstore(dstkey, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zinterstore(String dstkey, redis.clients.jedis.ZParams params, String... sets) {
    client.zinterstore(dstkey, params, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zinterstore(byte[] dstkey, redis.clients.jedis.ZParams params, byte[]... sets) {
    client.zinterstore(dstkey, params, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zunionstore(String dstkey, String... sets) {
    client.zunionstore(dstkey, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
    client.zunionstore(dstkey, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zunionstore(String dstkey, redis.clients.jedis.ZParams params, String... sets) {
    client.zunionstore(dstkey, params, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
    client.zunionstore(dstkey, params, sets);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> bgrewriteaof() {
    client.bgrewriteaof();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> bgsave() {
    client.bgsave();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<List<String>> configGet(String pattern) {
    client.configGet(pattern);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<String> configSet(String parameter, String value) {
    client.configSet(parameter, value);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> brpoplpush(String source, String destination, int timeout) {
    client.brpoplpush(source, destination, timeout);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
    client.brpoplpush(source, destination, timeout);
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public redis.clients.jedis.Response<String> configResetStat() {
    client.configResetStat();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> save() {
    client.save();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> lastsave() {
    client.lastsave();
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> publish(String channel, String message) {
    client.publish(channel, message);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> publish(byte[] channel, byte[] message) {
    client.publish(channel, message);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> randomKey() {
    client.randomKey();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<byte[]> randomKeyBinary() {
    client.randomKey();
    return getResponse(redis.clients.jedis.BuilderFactory.BYTE_ARRAY);
  }

  @Override
  public redis.clients.jedis.Response<String> flushDB() {
    client.flushDB();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> flushAll() {
    client.flushAll();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> info() {
    client.info();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  public redis.clients.jedis.Response<String> info(final String section) {
    client.info(section);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> dbSize() {
    client.dbSize();
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> shutdown() {
    client.shutdown();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> ping() {
    client.ping();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> select(int index) {
    client.select(index);
    redis.clients.jedis.Response<String> response = getResponse(redis.clients.jedis.BuilderFactory.STRING);
    client.setDb(index);

    return response;
  }

  @Override
  public redis.clients.jedis.Response<String> swapDB(int index1, int index2) {
    client.swapDB(index1, index2);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Long> bitop(redis.clients.jedis.BitOP op, byte[] destKey, byte[]... srcKeys) {
    client.bitop(op, destKey, srcKeys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
    client.bitop(op, destKey, srcKeys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterNodes() {
    client.clusterNodes();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterMeet(final String ip, final int port) {
    client.clusterMeet(ip, port);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterAddSlots(final int... slots) {
    client.clusterAddSlots(slots);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterDelSlots(final int... slots) {
    client.clusterDelSlots(slots);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterInfo() {
    client.clusterInfo();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<List<String>> clusterGetKeysInSlot(final int slot, final int count) {
    client.clusterGetKeysInSlot(slot, count);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterSetSlotNode(final int slot, final String nodeId) {
    client.clusterSetSlotNode(slot, nodeId);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterSetSlotMigrating(final int slot, final String nodeId) {
    client.clusterSetSlotMigrating(slot, nodeId);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> clusterSetSlotImporting(final int slot, final String nodeId) {
    client.clusterSetSlotImporting(slot, nodeId);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(String script) {
    return this.eval(script, 0, new String[0]);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(String script, List<String> keys, List<String> args) {
    String[] argv = redis.clients.jedis.Jedis.getParams(keys, args);
    return this.eval(script, keys.size(), argv);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(String script, int keyCount, String... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(redis.clients.jedis.BuilderFactory.EVAL_RESULT);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(String sha1) {
    return this.evalsha(sha1, 0, new String[0]);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(String sha1, List<String> keys, List<String> args) {
    String[] argv = Jedis.getParams(keys, args);
    return this.evalsha(sha1, keys.size(), argv);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(String sha1, int keyCount, String... params) {
    getClient(sha1).evalsha(sha1, keyCount, params);
    return getResponse(redis.clients.jedis.BuilderFactory.EVAL_RESULT);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(byte[] script) {
    return this.eval(script, 0);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(byte[] script, byte[] keyCount, byte[]... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(redis.clients.jedis.BuilderFactory.EVAL_BINARY_RESULT);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
    byte[][] argv = redis.clients.jedis.BinaryJedis.getParamsWithBinary(keys, args);
    return this.eval(script, keys.size(), argv);
  }

  @Override
  public redis.clients.jedis.Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
    getClient(script).eval(script, keyCount, params);
    return getResponse(redis.clients.jedis.BuilderFactory.EVAL_BINARY_RESULT);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(byte[] sha1) {
    return this.evalsha(sha1, 0);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
    byte[][] argv = BinaryJedis.getParamsWithBinary(keys, args);
    return this.evalsha(sha1, keys.size(), argv);
  }

  @Override
  public redis.clients.jedis.Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
    getClient(sha1).evalsha(sha1, keyCount, params);
    return getResponse(redis.clients.jedis.BuilderFactory.EVAL_BINARY_RESULT);
  }

  @Override
  public redis.clients.jedis.Response<Long> pfcount(String... keys) {
    client.pfcount(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> pfcount(final byte[]... keys) {
    client.pfcount(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
    client.pfmerge(destkey, sourcekeys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> pfmerge(String destkey, String... sourcekeys) {
    client.pfmerge(destkey, sourcekeys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<List<String>> time() {
    client.time();
    return getResponse(redis.clients.jedis.BuilderFactory.STRING_LIST);
  }

  @Override
  public redis.clients.jedis.Response<Long> touch(String... keys) {
    client.touch(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<Long> touch(byte[]... keys) {
    client.touch(keys);
    return getResponse(redis.clients.jedis.BuilderFactory.LONG);
  }

  @Override
  public redis.clients.jedis.Response<String> moduleUnload(String name) {
    client.moduleUnload(name);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<List<Module>> moduleList() {
    client.moduleList();
    return getResponse(redis.clients.jedis.BuilderFactory.MODULE_LIST);
  }

  @Override
  public redis.clients.jedis.Response<String> moduleLoad(String path) {
    client.moduleLoad(path);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }  
  
  @Override
  public redis.clients.jedis.Response<String> migrate(final String host, final int port, final int destinationDB,
                                                      final int timeout, final MigrateParams params, final String... keys) {
    client.migrate(host, port, destinationDB, timeout, params, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public redis.clients.jedis.Response<String> migrate(final String host, final int port, final int destinationDB,
                                                      final int timeout, final MigrateParams params, final byte[]... keys) {
    client.migrate(host, port, destinationDB, timeout, params, keys);
    return getResponse(redis.clients.jedis.BuilderFactory.STRING);
  }

  @Override
  public Response<Object> sendCommand(ProtocolCommand cmd, String... args){
    client.sendCommand(cmd, args);
    return getResponse(BuilderFactory.OBJECT);
  }
  
}
