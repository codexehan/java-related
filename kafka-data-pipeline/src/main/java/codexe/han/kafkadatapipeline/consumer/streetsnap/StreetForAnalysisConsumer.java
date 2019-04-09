package deja.fashion.datapipeline.consumer.streetsnap;

import deja.fashion.datapipeline.consumer.DataConsumer;
import deja.fashion.datapipeline.dto.streetsnap.StreetForAnalysisDTO;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.Deserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class StreetForAnalysisConsumer extends DataConsumer {

    public StreetForAnalysisConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }

    @Override
    public void consume(){
        while (true) {
            log.info("---------------Consume StreetForAnalysis Start------------------");
            ConsumerRecords<Long, StreetForAnalysisDTO> records = consumer.poll(Duration.ofSeconds(1));
            List<StreetForAnalysisDTO> streetForAnalysisDTOList = new ArrayList<>();
            for (ConsumerRecord<Long, StreetForAnalysisDTO> record : records) {
                log.info("time = %S, offset = %d, key = %s, value = %s%n", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                streetForAnalysisDTOList.add(record.value());
            }
            log.info("---------------Consume StreetForAnalysis Start------------------");

            log.info("---------------Write StreetForAnalysis Start------------------");
            toStreetForAnalysisEs(streetForAnalysisDTOList);
            log.info("---------------Write StreetForAnalysis End------------------");

        }
    }
    private void toStreetForAnalysisEs(List<StreetForAnalysisDTO> streetForAnalysisDTOList){
        try {
            BulkRequest request = new BulkRequest();
            for(StreetForAnalysisDTO streetForAnalysisDTO : streetForAnalysisDTOList) {
                //build upserts query
                IndexRequest indexRequest = new IndexRequest(index, type, String.valueOf(streetForAnalysisDTO.getId()))
                        .source(jsonBuilder()
                                .startObject()
                                .field("create_time", streetForAnalysisDTO.getCreateTime())
                                .field("delete", streetForAnalysisDTO.isDelete())
                                .field("eligible_item_count", streetForAnalysisDTO.getEligibleItemCount())
                                .field("image_height", streetForAnalysisDTO.getImageHeight())
                                .field("image_local_path", streetForAnalysisDTO.getImageLocalPath())
                                .field("image_original_url", streetForAnalysisDTO.getImageOriginalUrl())
                                .field("image_width", streetForAnalysisDTO.getImageWidth())
                                .field("influencer_id", streetForAnalysisDTO.getInfluencerId())
                                .field("is_recommended", false)
                                .field("last_update_time", streetForAnalysisDTO.getLastUpdateTime())
                                .field("like_count", streetForAnalysisDTO.getLikeCount())
                                .field("phash", streetForAnalysisDTO.getPhash())
                                .field("post_time", streetForAnalysisDTO.getPostTime())
                                .field("reference", streetForAnalysisDTO.getReference())
                                .field("review_status", streetForAnalysisDTO.getReviewStatus())
                                .field("street_for_analysis_id", streetForAnalysisDTO.getId())
                                .field("street_type", streetForAnalysisDTO.getStreetType())
                                .field("tags", streetForAnalysisDTO.getTags())
                                .endObject());
                UpdateRequest updateRequest = new UpdateRequest(index, type, String.valueOf(streetForAnalysisDTO.getId()))
                        .doc(jsonBuilder()
                                .startObject()
                                .field("create_time", streetForAnalysisDTO.getCreateTime())
                                .field("delete", streetForAnalysisDTO.isDelete())
                                .field("eligible_item_count", streetForAnalysisDTO.getEligibleItemCount())
                                .field("image_height", streetForAnalysisDTO.getImageHeight())
                                .field("image_local_path", streetForAnalysisDTO.getImageLocalPath())
                                .field("image_original_url", streetForAnalysisDTO.getImageOriginalUrl())
                                .field("image_width", streetForAnalysisDTO.getImageWidth())
                                .field("influencer_id", streetForAnalysisDTO.getInfluencerId())
                                .field("last_update_time", streetForAnalysisDTO.getLastUpdateTime())
                                .field("like_count", streetForAnalysisDTO.getLikeCount())
                                .field("phash", streetForAnalysisDTO.getPhash())
                                .field("post_time", streetForAnalysisDTO.getPostTime())
                                .field("reference", streetForAnalysisDTO.getReference())
                                .field("review_status", streetForAnalysisDTO.getReviewStatus())
                                .field("street_for_analysis_id", streetForAnalysisDTO.getId())
                                .field("street_type", streetForAnalysisDTO.getStreetType())
                                .field("tags", streetForAnalysisDTO.getTags())
                                .endObject())
                        .upsert(indexRequest);

                request.add(updateRequest);

            }
            log.info(" bulk api size is {}", streetForAnalysisDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }catch(Exception e){
            log.error("upsert ps relation error",e);
        }
    }
}
