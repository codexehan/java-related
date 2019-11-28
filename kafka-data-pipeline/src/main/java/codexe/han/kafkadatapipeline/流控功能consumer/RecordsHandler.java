package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RecordsHandler extends Thread{

    private RecordsPool recordsPool;

    public RecordsHandler(RecordsPool recordsPool){
        this.recordsPool = recordsPool;
    }

    @Override
    public void run(){
        //get records from pool
        //处理records
        //每次只拉取一个做处理，不知道会不会因为线程上下文切换，影响性能，需要做测试比较
        try {
            RecordNode recordNode = this.recordsPool.get();
            /*ConsumerRecord<String,String> consumerRecord = recordNode.consumerRecord;
            //handle consumer record
            for(int i=0;i<9999999999L;i++){

            }
            this.recordsPool.ack(recordNode);*/
        } catch (Exception e) {
            log.error("get record from records pool error",e);
        }

        /*for(TopicPartition tp : records.partitions()){
            List<ConsumerRecord<String, String>> tpRecords = records.records(tp);
            //处理tp records
            long lastConsumedOffset = tpRecords.get(tpRecords.size()-1).offset();
            synchronized(offsets){
                if(!offsets.containsKey(tp)){
                    offsets.put(tp,new OffsetAndMetadata(lastConsumedOffset + 1));
                }
                else{
                    long position = offsets.get(tp).offset();
                    if(position < lastConsumedOffset+1){
                        offsets.put(tp,new OffsetAndMetadata(lastConsumedOffset+1));
                    }
                }
            }
        }*/
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
            if(recordNodes!=null) {
                List<ConsumerRecord<String,String>> consumerRecords = recordNodes.stream().map(t->t.consumerRecord).collect(Collectors.toList());
                //handle consumer record
                for(ConsumerRecord record: consumerRecords) {
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
