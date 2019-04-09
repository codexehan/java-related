package deja.fashion.datapipeline.test.useless;

import deja.fashion.datapipeline.client.ElasticsearchClient;
import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.dto.influencer.InfluencerStyleRelationDTO;
import deja.fashion.datapipeline.dto.inventory.ProductInventoryComboDTO;
import deja.fashion.datapipeline.properties.DataPipelineProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class InfluencerStyleRelationConsumer {

    @Autowired
    private DataPipelineProperties dataPipelineProperties;

    private KafkaConsumer<Long, InfluencerStyleRelationDTO> consumer;

    private ElasticsearchClient elasticsearchClient;

    private InfluencerStyleRelationConsumer(String bootstrapServers, String groupId){
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.dataPipelineProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.dataPipelineProperties.getGroupId());
        //props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, new ProductPriceJsonSerde<ProductInventoryComboDTO>());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024 * 1024 * 2);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000 * 5);
        //       props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerCount);
        this.consumer =  new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(Constants.KAFKA_TOPIC_INFLUENCER_STYLE_RELATION));
    }


    public void consume(){
        while (true) {
            log.info("---------------Consume Influencer Style Relation Start------------------");
            ConsumerRecords<Long, InfluencerStyleRelationDTO> records = consumer.poll(Duration.ofSeconds(1));
            List<InfluencerStyleRelationDTO> influencerDTOList = new ArrayList<>();
            for (ConsumerRecord<Long, InfluencerStyleRelationDTO> record : records) {
                log.info("time = %S, offset = %d, key = %s, value = %s%n", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                influencerDTOList.add(record.value());
            }
            log.info("---------------Consume Influencer Style Relation Start------------------");

            log.info("---------------Write Influencer Style Relation ES Start------------------");
            toInfluencerStyleRelationEs(influencerDTOList);
            log.info("---------------Write Influencer Style Relation ES End------------------");

        }
    }


    private void toInfluencerStyleRelationEs(List<InfluencerStyleRelationDTO> influencerDTOList){
        try {
            BulkRequest request = new BulkRequest();
            for(InfluencerStyleRelationDTO influencerStyleRelationDTO : influencerDTOList) {
                //build upserts query
                IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT_INVENTORY, Constants.ES_TYPE_DEJA_PRODUCT_INVENTORY, String.valueOf(influencerStyleRelationDTO.getId()))
                        .source(jsonBuilder()
                                .startObject()
                                .field("create_time", influencerStyleRelationDTO.getCreateTime())
                                .field("influecer_style_type", influencerStyleRelationDTO.getInfluencerStyleType())
                                .field("influencer_id", influencerStyleRelationDTO.getInfluencerId())
                                .field("influencer_style_id", influencerStyleRelationDTO.getInfluencerStyleId())
                                .field("influencer_style_name", influencerStyleRelationDTO.getInfluencerStyleName())
                                .field("influencer_style_relation_id", influencerStyleRelationDTO.getId())
                                .field("last_update_time", influencerStyleRelationDTO.getLastUpdateTime())
                                .endObject());
                request.add(indexRequest);
            }
            log.info("influencer style relation bulk api size is {}", influencerDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("influencer style relation bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }catch(Exception e){
            log.error("upsert influencer style relation error",e);
        }
    }
}
