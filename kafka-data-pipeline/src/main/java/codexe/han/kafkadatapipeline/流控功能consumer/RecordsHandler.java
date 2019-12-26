package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RecordsHandler extends Thread{

    private RecordsPool recordsPool;
    private int MAX_BATCH_SIZE = 100;

    private int MAX_RETRY = 3; //重试失败 发送到 RETRY_CONSUMER_GROUP topic

    public RecordsHandler(RecordsPool recordsPool){
        this.recordsPool = recordsPool;
    }
    public RecordsHandler(RecordsPool recordsPool, int MAX_BATCH_SIZE, int MAX_RETRY){
        this.recordsPool = recordsPool;
        this.MAX_BATCH_SIZE = MAX_BATCH_SIZE;
        this.MAX_RETRY = MAX_RETRY;
    }

    @Override
    public void run(){
        //get records from pool
        //处理records
        //每次只拉取一个做处理，不知道会不会因为线程上下文切换，影响性能，需要做测试比较
        try {
            handleByBatch(this.MAX_BATCH_SIZE);
        } catch (Exception e) {
            log.error("get record from records pool error",e);
        }
    }

    /**
     * 一个一个的处理，会减少重复消费的几率
     */
    public void handleOneByOne(){
        try {
            RecordNode recordNode = this.recordsPool.get();
            if(recordNode != null) {
                ConsumerRecord<String, String> consumerRecord = recordNode.consumerRecord;
                //handle consumer record
                for (int i = 0; i < 9999999999L; i++) {

                }
                this.recordsPool.ack(recordNode);
            }
        } catch (Exception e) {
            log.error("get record from records pool error",e);
        }
    }

    /**
     * 一旦出现问题，需要从batch起始位置重新消费
     * @param maxBatchSize
     */
    public void handleByBatch(int maxBatchSize){
        try {
            List<RecordNode> recordNodes = this.recordsPool.get(maxBatchSize);
            log.info("handle by batch");
            if(recordNodes!=null) {
                List<ConsumerRecord> consumerRecords = recordNodes.stream().map(t->t.consumerRecord).collect(Collectors.toList());
                //handle consumer record
                for(ConsumerRecord record: consumerRecords) {
                    log.info("record : {} is processing", record);
                    for (int i = 0; i < 9999999999L; i++) {

                    }
                }
                this.recordsPool.ack(recordNodes);
            }
        } catch (Exception e) {
            log.error("get record from records pool error",e);
        }
    }
}
