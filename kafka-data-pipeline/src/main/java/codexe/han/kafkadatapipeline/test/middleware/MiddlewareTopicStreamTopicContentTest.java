package codexe.han.kafkadatapipeline.test.middleware;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

@Slf4j
public class MiddlewareTopicStreamTopicContentTest {
    public static void main(String[] args) {
    //    String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
     //   String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        String bootstrapServer = "10.60.7.119:9092,10.60.7.119:9091,10.60.7.119:9090";

        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
   //     String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE;
     //   String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE;
     //   String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE;
        String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS;
   //     String topic = Constants.KAFKA_TOPIC_PRODUCT_STATUS_CHANGE;
    //    String topic = Constants.KAFKA_TOPIC_PRODUCT_INVENTORY;
   //     String topic = Constants.KAFKA_TOPIC_PRODUCTS;
   //     String topic = Constants.KAFKA_TOPIC_PRODUCT_PRICE;
    //    String topic = Constants.KAFKA_TOPIC_STREET_ITEM_REFRESH;
    //    String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE;
   //     String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE;
        String type = "tags";

        String groupId = "data-synchronize-dev";
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        KafkaConsumer consumer = new KafkaConsumer(props, new LongDeserializer(), new StringDeserializer());
   //     consumer.subscribe(Arrays.asList(topic));

    //    consumer.poll(0);
        // Now there is heartbeat and consumer is "alive"
        Collection<TopicPartition> topicPartitionCollection = new ArrayList<>();
        topicPartitionCollection.add(new TopicPartition(topic, 0));
        consumer.assign(topicPartitionCollection);
        consumer.seek(new TopicPartition(topic, 0),27477057);//24479813

        int size = 0;
        log.info("---------------Consume Product Price Start------------------");
        while (true) {
            ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Long, String> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                if(record.key().equals(5604439L)){
                    log.info("has inventory");
                }
                if(record.value()==null || record.value().equals("{}")){
                    log.info("value is null");
                }
                if(record.value().contains("\"purchasable\":true")){
                    size++;
                }
            }

            if(!records.isEmpty()){
                consumer.commitSync();
            }
        }
    }
}
