package deja.fashion.datapipeline.test;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.consumer.product.ProductConsumer;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import deja.fashion.datapipeline.service.Impl.PipelineKafkaOffsetServiceImpl;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class TestProductConsumer {
    public static void main(String[] args) {
         //   String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
            String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        //String bootstrapServer = "10.60.7.119:9092,10.60.7.119:9091,10.60.7.119:9090";

        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        //     String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE;
        //   String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE;
        //   String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE;
        //   String topic = Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS;
        //    String topic = Constants.KAFKA_TOPIC_PRODUCT_STATUS_CHANGE;
        //    String topic = Constants.KAFKA_TOPIC_PRODUCT_INVENTORY;
             String topic = Constants.KAFKA_TOPIC_PRODUCTS;
        //     String topic = Constants.KAFKA_TOPIC_PRODUCT_PRICE;
       // String topic = Constants.KAFKA_TOPIC_STREET_ITEM_REFRESH;
        String type = "tags";

        String groupId = "data-synchronize-dev";
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        //KafkaConsumer consumer = new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
        KafkaConsumer consumer = new KafkaConsumer(props, new StringDeserializer(),  DejaJsonSerde.<ProductDTO>builder().clazz(ProductDTO.class).build());
        //     consumer.subscribe(Arrays.asList(topic));

        //    consumer.poll(0);
        // Now there is heartbeat and consumer is "alive"
        Collection<TopicPartition> topicPartitionCollection = new ArrayList<>();
        topicPartitionCollection.add(new TopicPartition(topic, 0));
        consumer.assign(topicPartitionCollection);
        consumer.seek(new TopicPartition(topic, 0),616964);

        log.info("---------------Consume Product Price Start------------------");
        while (true) {
            ConsumerRecords<String, ProductDTO> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, ProductDTO> record : records) {
                if(record.key().equals("5833570")){
                    log.info("has inventory");
                }
                if(record.value()==null || record.value().equals("{}")){
                    log.info("value is null");
                }
                log.info("time = {}, offset = {}, key = {}, value = {}", new Date(record.timestamp()), record.offset(), record.key(), record.value());
            }

            if(!records.isEmpty()){
                consumer.commitSync();
            }
        }
    }

}
