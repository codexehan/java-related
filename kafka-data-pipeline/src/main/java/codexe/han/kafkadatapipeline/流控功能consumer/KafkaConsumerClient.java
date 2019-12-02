package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaConsumerClient {
    public static final String brokerList ="";
    private KafkaConsumer kafkaConsumer;
    private ExecutorService executorService;
    private AtomicBoolean running;
    private Map<TopicPartition, OffsetAndMetadata> offsets;//max
    private RecordsPool recordsPool;
    public KafkaConsumerClient(Properties props, String topic, int threadNumber, int maxPendingConsumingTask, int maxOffsetGap){
        this.kafkaConsumer = new KafkaConsumer(props);
        this.kafkaConsumer.subscribe(Arrays.asList(topic));
        this.executorService = new ThreadPoolExecutor(threadNumber,threadNumber,0L, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(1000),new ThreadPoolExecutor.CallerRunsPolicy());
        this.running.set(true);
        this.offsets = new HashMap<>();
        this.recordsPool = new RecordsPool(maxPendingConsumingTask,maxOffsetGap);
    }

    public void consume(){
        try {
            while (this.running.get()) {
                //commit offset
                synchronized(offsets){
                    this.recordsPool.loadOffset(offsets);
                    if(!offsets.isEmpty()){
                        this.kafkaConsumer.commitSync(offsets);
                        offsets.clear();
                    }
                }
                //check whether need to start flow control
                while(!this.recordsPool.needFlowControl());
                try{
                    while(true){
                        ConsumerRecords<String,String> records = this.kafkaConsumer.poll(Duration.ofMillis(1000));
                        if(!records.isEmpty()){
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
