package codexe.han.kafkadatapipeline.basic;

import codexe.han.kafkadatapipeline.basic.producer.DejaKafkaProducer;
import codexe.han.kafkadatapipeline.common.Constants;
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
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

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
 *
 * 源码解析：
 * 【change_log offset存在客户端本地】
 * change_log的topic消费，offset是记录在客户端本地的
 * OffsetCheckpoint 管理topic[change_log] partition offset 读写到一个文件当中就是.checkpoint, topic的名字就是change_log
 * 这个文件位于客户端，state.dir(默认值 /tmp/kafka-streams, confluence 上面写的是位于/var/lib/kafka-streams)规定的路径下面
 * 查看该文件内容 strings kafka-streams/[star*]/[star*]/.checkpoint
 *
 * 先初始化consumer
 * 然后开始streaming
 * org.apache.kafka.clients.consumer.internals.ConsumerCoordinator 负责初始化consumer offset
 * offset存储在内部topic __consumer_offsets上面 有50个partition
 * offset reset
 * OffsetResetStrategy 保存默认offset strategy
 *
 * KafkaConsumer.poll中调用过程 -> timeout的意思是，如果有结果会立刻返回，如果没有结果，会block一段时间知道timeout
 * Fetcher.parseCompletedFetch 会对响应进行判断，如果broker 返回 offset有错，就会调用默认reset策略
 * KafkaConsumer.updateAssignmentMetadataIfNeeded检测是否需要reset offset 需要的话 会根据策略earlist 或者 latest
 * reset 会调用Fetcher.resetOffsetsIfNeeded->Fetcher.resetOffsetsAsync->Fetcher.sendListOffsetRequest 获取某个partition的offset
 *  private Long offsetResetStrategyTimestamp(final TopicPartition partition) {
 *         OffsetResetStrategy strategy = subscriptions.resetStrategy(partition);
 *         if (strategy == OffsetResetStrategy.EARLIEST)
 *             return ListOffsetRequest.EARLIEST_TIMESTAMP;//-2
 *         else if (strategy == OffsetResetStrategy.LATEST)
 *             return ListOffsetRequest.LATEST_TIMESTAMP;//-1
 *         else
 *             return null;
 *     }
 * Fetcher.sendListOffsetRequest根据timestamp请求offset,如果是earliest策略,就是-2;latest就是-1
 * Fetcher根据offset从broker进行消费
 */
@Slf4j
public class StreamOffsetTest {

    public static final String topic1 = "stream_test_1";
    public static final String topic2 = "stream_test_2";
    public static final String topic3 = "stream_join";

    public static final String bootstrapServers = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";

    public static void main(String[] args){
      //  joinStream();
        singleStream();
    }

    public static void singleStream(){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "joinStream_2");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams");
  //      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // create store
        StoreBuilder<KeyValueStore<String,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore("joinStream_2_state_store"), Serdes.String(), Serdes.String());
       /* StoreBuilder<KeyValueStore<Long,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE), Serdes.Long(), Serdes.String());*/
        // register store
        streamsBuilder.addStateStore(keyValueStoreBuilder);

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
