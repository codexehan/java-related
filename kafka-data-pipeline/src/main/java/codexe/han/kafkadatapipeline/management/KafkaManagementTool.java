package codexe.han.kafkadatapipeline.management;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.Properties;

@Slf4j
public class KafkaManagementTool {
    public static void main(String[] args) {
       /* String zookeeperConnect = "zkserver1:2181,zkserver2:2181";
        int sessionTimeoutMs = 10 * 1000;
        int connectionTimeoutMs = 8 * 1000;
        // Note: You must initialize the ZkClient with ZKStringSerializer.  If you don't, then
        // createTopic() will only seem to work (it will return without error).  The topic will exist in
        // only ZooKeeper and will be returned when listing topics, but Kafka itself does not create the
        // topic.
        ZkClient zkClient = new ZkClient(
                zookeeperConnect,
                sessionTimeoutMs,
                connectionTimeoutMs,
                ZKStringSerializer$.MODULE$);

        // Security for Kafka was added in Kafka 0.9.0.0
        boolean isSecureKafkaCluster = false;
        ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(zookeeperConnect), isSecureKafkaCluster);

        String topic = "my-topic";
        int partitions = 2;
        int replication = 3;
        Properties topicConfig = new Properties(); // add per-topic configurations settings here
        AdminUtils.createTopic(zkUtils, topic, partitions, replication, topicConfig);

        AdminUtils.createTopic$default$5();
        zkClient.close();*/
    }
}
