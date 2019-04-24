package codexe.han.kafkadatapipeline.basic;

import codexe.han.kafkadatapipeline.basic.producer.DejaKafkaProducer;
import codexe.han.kafkadatapipeline.dto.product.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

/**
 * 用stream的时候要注意一个问题，如果我们上游的topic采用的是compact模式，很可能stream consumer对应的offset实际上不存在了，
 * 这样就会触发stream对应的ConsumerConfig.AUTO_OFFSET_RESET_CONFIG设置，默认是earliest(正常consumer是latest)
 * 所以会造成很大的数据重复消费
 * 但是如果设置成latest，就需要考虑消息丢失的可能
 */
@Slf4j
public class StreamOffsetTest {

    public static final String topic1 = "stream_test_1";
    public static final String topic2 = "stream_test_2";
    public static final String topic3 = "stream_join";

    public static final String bootstrapServers = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";

    public static void main(String[] args){
        joinStream();
     //   singleStream();
    }

    public static void singleStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "joinStream_2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");
  //      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KTable<String, String> productValidateTable = streamsBuilder.table(topic1, Consumed.with(Serdes.String(), Serdes.String()));
        productValidateTable.toStream().to(topic3, Produced.with(Serdes.String(), Serdes.String()));
        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("single stream running ...");
        stream.start();
    }

    public static void joinStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "joinStream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");
   //     props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();


        KTable<String, String> productValidateTable = streamsBuilder.table(topic1, Consumed.with(Serdes.String(), Serdes.String()));
        KTable<String, String> priceValidateTable = streamsBuilder.table(topic2, Consumed.with(Serdes.String(), Serdes.String()));
        //3.1.2 remove
        //       KTable<Long, String> inventoryValidateTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE, Consumed.with(Serdes.Long(), Serdes.String()));

        KTable<String, String> finalStatusTable = productValidateTable.leftJoin(priceValidateTable, (leftValue, rightValue)->{
            if(rightValue == null){//no price info
                return "no right";
            }
            else{
                return "both";
            }
        });

        finalStatusTable.toStream().to(topic3, Produced.with(Serdes.String(), Serdes.String()));

        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("ProductValidateMiddleware running ...");
        stream.start();
    }




}

@Slf4j
class Producer{
    public static void main(String[] args) {
        createTwoTopic();
    }
    public static void createTwoTopic(){
        try(DejaKafkaProducer dejaKafkaProducer = DejaKafkaProducer.builder().bootstrapServers(StreamOffsetTest.bootstrapServers).build()){
            for(int i = 0; i<10; i++){
                dejaKafkaProducer.send(StreamOffsetTest.topic1, String.valueOf(i), UUID.randomUUID().toString());
                dejaKafkaProducer.send(StreamOffsetTest.topic2, String.valueOf(i), UUID.randomUUID().toString());
            }
        } catch (IOException e) {
            log.error("send kafka message error ",e);
        }
    }
}

@Slf4j
class Consumer{
    public static void main(String[] args) {
        getJoinResult();
    }

    public static void getJoinResult(){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, StreamOffsetTest.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "join_group_1");
        //   props.put("enable.auto.commit", "false");
        KafkaConsumer consumer = new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(Arrays.asList(StreamOffsetTest.topic3));

        while (true) {
            long nextOffset = -1;
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", record.timestamp(), record.offset(), record.key(), record.value());
            }
        }
    }
}
