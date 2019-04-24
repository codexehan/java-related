package codexe.han.kafkadatapipeline.consumer.product;

import codexe.han.kafkadatapipeline.consumer.DataConsumer;
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
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class ProductPurchasableConsumer extends DataConsumer {
    public ProductPurchasableConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde) {
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde);
    }
    @Override
    public void consume(){
        while (true) {
            log.info("---------------Consume Product Purchasable Start------------------");
            ConsumerRecords<Long, String> records = consumer.poll(Duration.ofSeconds(1));
            Map<Long, Boolean> productPurchasableMap = new HashMap<>();
            for (ConsumerRecord<Long, String> record : records) {
                log.info("time = %S, offset = %d, key = %s, value = %s%n", new Date(record.timestamp()), record.offset(), record.key(), record.value());
                productPurchasableMap.put(record.key(), Boolean.valueOf(record.value()));
            }
            log.info("---------------Consume Product Purchasable Start------------------");

            log.info("---------------Write Product Purchasable ES Start------------------");
            toProductEs(productPurchasableMap);
            log.info("---------------Write Product Purchasable ES End------------------");
        }
    }


    private void toProductEs(Map<Long, Boolean> productPurchsableMap){
        /**
         *  useless
         * .field("all_size", false)
         * .field("in_stock", )
         * .field("validate_message", )
         * .field("validate_status", )
         */
        try {
            BulkRequest request = new BulkRequest();
            for(Map.Entry entry : productPurchsableMap.entrySet()) {
                //build upserts query
                IndexRequest indexRequest = new IndexRequest(index, type, String.valueOf(entry.getKey()))
                        .source(jsonBuilder()
                                .startObject()
                                .field("autotag_version", 0)
                                .field("brand_id",0)
                                .field("brand_name", "to be delivered from data group")
                                .field("brand_url", "")
                                .field("breadcrumb", "")
                                .field("category", 0)
                                .field("cloth_id", 0)
                                .field("color", 0)
                                .field("color_and_pattern",0)
                                .field("create_time", 0)
                                .field("currency", "")
                                .field("current_price", "")
                                .field("deli_color_top2_score_1", 0)
                                .field("deli_color_top2_score_2",0)
                                .field("deli_color_top2_score_id_1", 0)
                                .field("deli_color_top2_score_id_2", 0)
                                .field("description", "")
                                .field("detail_description", "")
                                .field("discount_percentage", 0)
                                .field("height", 0)
                                .field("image_url", 0)
                                .field("is_delete", true)
                                .field("is_discount", false)
                                .field("is_new_arrival", false)
                                .field("is_ocb", false)
                                .field("is_purchasable", entry.getValue())
                                .field("is_recommend", true)
                                .field("last_update_time", 0)
                                .field("length", 0)
                                .field("merchant_id", 0)
                                .field("neckline", 0)
                                .field("original_price", 0)
                                .field("pattern", 0)
                                .field("popularity", 0)
                                .field("product_code", "")
                                .field("product_color", "")
                                .field("product_group_id", "")
                                .field("product_id", 0)
                                .field("product_name", "")
                                .field("recommend_reason", "")
                                .field("site_mark", "")
                                .field("size_guide_table", "")
                                .field("sleeve_length", "")
                                .field("subcategory", 0)
                                .field("weight", 10000)
                                .field("width", 0)
                                .endObject());
                UpdateRequest updateRequest = new UpdateRequest(index, type, String.valueOf(entry.getKey()))
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_purchasable", entry.getValue())
                                .endObject())
                        .upsert(indexRequest);

                request.add(updateRequest);

            }
            log.info("product purchasable bulk api size is {}", productPurchsableMap.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product purchasable bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }catch(Exception e){
            log.error("upsert product error",e);
        }
    }

}
