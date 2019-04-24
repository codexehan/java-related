package codexe.han.kafkadatapipeline.test.useless;

import codexe.han.kafkadatapipeline.client.ElasticsearchClient;
import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.influencer.InfluencerDTO;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryComboDTO;
import codexe.han.kafkadatapipeline.properties.DataPipelineProperties;
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
public class InfluencerConsumer {

    @Autowired
    private DataPipelineProperties dataPipelineProperties;

    private KafkaConsumer<Long, InfluencerDTO> consumer;

    private ElasticsearchClient elasticsearchClient;

    private InfluencerConsumer(String bootstrapServers, String groupId){
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
        consumer.subscribe(Arrays.asList(Constants.KAFKA_TOPIC_INFLUENCER));
    }


    public void consume(){
        while (true) {
            log.info("---------------Consume Product Price Start------------------");
            ConsumerRecords<Long, InfluencerDTO> records = consumer.poll(Duration.ofSeconds(1));
            List<InfluencerDTO> influencerDTOList = new ArrayList<>();
            for (ConsumerRecord<Long, InfluencerDTO> record : records) {
                log.info("time = %S, offset = %d, key = %s, value = %s%n", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                influencerDTOList.add(record.value());
            }
            log.info("---------------Consume Product Price Start------------------");

            log.info("---------------Write Product Price ES Start------------------");
            toInfluencerEs(influencerDTOList);
            log.info("---------------Write Product Price ES End------------------");

        }
    }


    private void toInfluencerEs(List<InfluencerDTO> influencerDTOList){
        try {
            BulkRequest request = new BulkRequest();
            for(InfluencerDTO influencerDTO : influencerDTOList) {
                //build upserts query
                IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT_INVENTORY, Constants.ES_TYPE_DEJA_PRODUCT_INVENTORY, String.valueOf(influencerDTO.getId()))
                        .source(jsonBuilder()
                                .startObject()
                                .field("account_type", influencerDTO.getAccountType())
                                .field("avatar_local_path", influencerDTO.getAvatarLocalPath())
                                .field("avatar_original_url", influencerDTO.getAvatarOriginalUrl())
                                .field("create_time", influencerDTO.getCreateTime())
                                .field("delete", influencerDTO.isDelete())
                                .field("display_name", influencerDTO.getDisplayName())
                                .field("follower_count", 0)
                                .field("influencer_id", influencerDTO.getId())
                                .field("is_delete", influencerDTO.isDelete())
                                .field("last_update_time", influencerDTO.getLastUpdateTime())
                                .field("original_name", influencerDTO.getOriginalName())
                                .field("platform", influencerDTO.getPlatform())
                                .field("race", influencerDTO.getRace())
                                .field("reference_id", influencerDTO.getReferenceId())
                                .field("region", influencerDTO.getRegion())
                                .field("source_url", "")
                                .field("vip_status", influencerDTO.getVipStatus())
                                .endObject());
                request.add(indexRequest);
            }
            log.info("influencer bulk api size is {}", influencerDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("influencer bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }catch(Exception e){
            log.error("upsert influencer error",e);
        }
    }
}
