package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaConsumerClient {
    public static final String brokerList ="";
    private KafkaConsumer kafkaConsumer;
    private ExecutorService executorService;
    private AtomicBoolean running = new AtomicBoolean();
    private Map<TopicPartition, OffsetAndMetadata> offsets;//max
    private RecordsPool recordsPool;
    public KafkaConsumerClient(Properties props, List<String> topicList, int threadNumber, int maxPendingConsumingTask, int maxOffsetGap){
        this.executorService = new ThreadPoolExecutor(threadNumber,threadNumber,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(1000),new ThreadPoolExecutor.CallerRunsPolicy());
        this.running.set(true);
        this.offsets = new HashMap<>();
        this.recordsPool = new RecordsPool(maxPendingConsumingTask,maxOffsetGap);
        this.kafkaConsumer = new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
        this.kafkaConsumer.subscribe(topicList, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                //如果发生再均衡，消费线程还在处理消费业务，那么可以考虑直接移除RecordsPool中partition中的数目
                //before rebalance
                //commit offset
                log.info("rebalance start");
                synchronized(offsets){
                    recordsPool.loadOffset(offsets);
                    if(!offsets.isEmpty()){
                        log.info("commit offset sync {}",offsets);
                        kafkaConsumer.commitSync(offsets);
                        offsets.clear();
                    }
                }
                //直接销毁线程池，重新创建
                log.info("destroy old thread pool");
                executorService.shutdownNow();
                log.info("destroy the records pool");
                recordsPool = null;
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                //after rebalance and before start consume
                log.info("rebalance finished and do the initialization again");
                log.info("initialize thread pool");
                executorService = new ThreadPoolExecutor(threadNumber,threadNumber,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(1000),new ThreadPoolExecutor.CallerRunsPolicy());
                log.info("initialize records pool");
                recordsPool = new RecordsPool(maxPendingConsumingTask,maxOffsetGap);
            }
        });
    }

    public void consume(){
        try {
            while (this.running.get()) {
                //commit offset
                synchronized(offsets){
                    this.recordsPool.loadOffset(offsets);
                    if(!offsets.isEmpty()){
                        log.info("commit offset sync {}",offsets);
                        this.kafkaConsumer.commitSync(offsets);
                        offsets.clear();
                    }
                }
                //check whether need to start flow control
                while(this.recordsPool.needFlowControl()){
                    log.info("flow controlling");
                }
                try{
                    while(true){
                        ConsumerRecords<String,String> records = this.kafkaConsumer.poll(Duration.ofMillis(1000));
                        if(!records.isEmpty()){
                            log.info("pull message from kafka : {}",records);
                            this.executorService.submit(new RecordsHandler(this.recordsPool));
                        }
                    }
                }catch(Exception e){
                    log.error("consume consumer records error",e);
                }
            }
        }catch(Exception e){
            log.error("kafka consume error",e);
        }
    }

    public void stop(){
        this.running.set(false);
    }

}
