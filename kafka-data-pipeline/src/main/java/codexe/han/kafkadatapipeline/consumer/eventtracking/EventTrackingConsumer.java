package codexe.han.kafkadatapipeline.consumer.eventtracking;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import codexe.han.kafkadatapipeline.consumer.DataConsumer;
import codexe.han.kafkadatapipeline.dto.eventtracking.EventTrackingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Deserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class EventTrackingConsumer extends DataConsumer {
    public EventTrackingConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde) {
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde);
    }

    public void consume(){
        log.info("---------------Consume Event Tracking Start------------------");
        List<EventTrackingEvent> eventTrackingEventList = new ArrayList<>();

        openElasticClient();
        while (true) {
            ConsumerRecords<String, EventTrackingEvent> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, EventTrackingEvent> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                try {
                    record.value().setId(record.key());
                    eventTrackingEventList.add(record.value());
                }catch(Exception e){
                    log.error("event tracking error {}", e);
                }
            }
            if(!eventTrackingEventList.isEmpty()){
                try {
                    keepAlive();

                    toEs(eventTrackingEventList);
               //     closeElasticClient();
                }catch(Exception e){
                    log.error("event tracking io exception {}", e);
                }
            }
            eventTrackingEventList.clear();
            consumer.commitSync();
        }
    }

    @Retryable(value = {IOException.class})
    public void toEs(List<EventTrackingEvent> eventTrackingEventList) throws IOException {
        try{
            BulkRequest request = new BulkRequest();
            for(EventTrackingEvent event : eventTrackingEventList) {
                try {
                    IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_EVENT_TRACKING, Constants.ES_TYPE_DEJA_EVENT_TRACKING, event.getId())
                            .source(jsonBuilder()
                                    .startObject()
                                    .field("event_name", event.getEventName())
                                    .field("uid", event.getEventBody().getUid())
                                    .field("optional", DejaUtils.toJson(event.getEventBody().getOptional()))
                                    .field("date", event.getEventTime() != null ? new Date(event.getEventTime()) : null)
                                    .field("timestamp", event.getEventTime())
                                    .field("event_version", event.getEventVersion())
                                    .endObject());
                    request.add(indexRequest);
                }catch (Exception e){
                    log.error("event tracking has format error, skip it {}",e);
                }
            }
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request, RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("event tracking bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("write event tracking index error",e);
        }
    }

}
