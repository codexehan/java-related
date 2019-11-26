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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class KafkaConsumerClient {
    public static final String brokerList ="";
    private KafkaConsumer kafkaConsumer;
    private ExecutorService executorService;
    private AtomicBoolean running;
    private Map<TopicPartition, OffsetAndMetadata> offsets;//max
    private int maxPendingConsumingTask;
    private int maxOffsetGap;
    private AtomicInteger maxOffsetPending;
    private AtomicInteger minOffsetPending;
    private LinkedBlockingDeque pendingTaskQueue;
    public KafkaConsumerClient(Properties props, String topic, int threadNumber, int maxPendingConsumingTask, int maxOffsetGap){
        this.kafkaConsumer = new KafkaConsumer(props);
        this.kafkaConsumer.subscribe(Arrays.asList(topic));
        this.pendingTaskQueue = new LinkedBlockingDeque();
        this.executorService = new ThreadPoolExecutor(threadNumber,threadNumber,0L, TimeUnit.MILLISECONDS,pendingTaskQueue,new ThreadPoolExecutor.CallerRunsPolicy());
        this.running.set(true);
        this.offsets = new HashMap<>();
        this.maxPendingConsumingTask = maxPendingConsumingTask;
        this.maxOffsetGap = maxOffsetGap;
    }

    public void consume(){
        try {
            while (this.running.get()) {
                //commit offset
                synchronized(offsets){
                    if(!offsets.isEmpty()){
                        this.kafkaConsumer.commitSync(offsets);
                        offsets.clear();
                    }
                }
                //check whether need to start flow control
                while(!needControlFlow());
                try{
                    while(true){
                        ConsumerRecords<String,String> records = this.kafkaConsumer.poll(Duration.ofMillis(1000));
                        if(!records.isEmpty()){
                            this.executorService.submit(new RecordsHandler(records,offsets));
                        }
                    }
                }

            }
        }catch(Exception e){
            log.error("kafka consume error",e);
        }
    }

    public boolean needControlFlow(){
        if(this.maxOffsetPending.get() - this.minOffsetPending.get()>=this.maxOffsetGap){
            log.warn("Start control flow, Max pending offset is {}, min pending offset is {}, which are larger than Max Offset Gap setting {}", this.maxOffsetPending, this.minOffsetPending, this.maxOffsetGap);
            return true;
        }
        else if(this.pendingTaskQueue.size()>=this.maxPendingConsumingTask){
            log.warn("Start control flow, Pending task number is {}, which are larger than Max Pending Consuming Task setting {}", this.pendingTaskQueue.size(), this.maxPendingConsumingTask);
            return true;
        }
        return false;
    }
}
