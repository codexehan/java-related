package codexe.han.kafkadatapipeline.流控功能consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.List;
import java.util.Map;

@Slf4j
public class RecordsHandler extends Thread{

    public final ConsumerRecords<String, String> records;
    private Map<TopicPartition, OffsetAndMetadata> offsets;

    public RecordsHandler(ConsumerRecords<String, String> records, Map<TopicPartition, OffsetAndMetadata> offsets){
        this.records = records;
        this.offsets = offsets;
    }

    @Override
    public void run(){
        //处理records
        for(TopicPartition tp : records.partitions()){
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
        }
    }
}
