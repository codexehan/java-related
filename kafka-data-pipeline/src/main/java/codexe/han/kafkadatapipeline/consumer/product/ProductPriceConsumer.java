package codexe.han.kafkadatapipeline.consumer.product;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import codexe.han.kafkadatapipeline.consumer.DataConsumer;
import codexe.han.kafkadatapipeline.dto.ProductPriceDTO;
import codexe.han.kafkadatapipeline.dto.product.ProductDTO;
import codexe.han.kafkadatapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class ProductPriceConsumer extends DataConsumer {

    public ProductPriceConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }


    public void consume(){
        log.info("---------------Consume Product Price Start------------------");
        List<ProductPriceDTO> productDTOList = new ArrayList<>();
        openElasticClient();
        while (!exit) {
            long nextOffset = -1;
            ConsumerRecords<String, ProductPriceDTO> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, ProductPriceDTO> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}",record.timestamp(), record.offset(), record.key(), record.value());
                nextOffset = record.offset()+1;
                if(record.value()!=null) {
                    productDTOList.add(record.value());
                }
                else{
                    log.error("product price format has error");
                }
            }

            if(!productDTOList.isEmpty()) {
                log.info("---------------Write Product Price ES Start------------------");
                try {
                    keepAlive();

                    toProductEs(productDTOList);

                    validatePrice(productDTOList);
                }catch(Exception e){
                    log.error("price io exception {}", e);
                }
                log.info("---------------Write Product Price ES End------------------");
            }

            productDTOList.clear();
            if(nextOffset>0){
                log.info("update next offset {}", nextOffset);
                this.pipelineKafkaOffsetService.updateOffset(pipelineKafkaOffset.getId(),nextOffset);
                //closeElasticClient();
            }
        }
    }

    @Retryable(value = {IOException.class})
    private void toProductEs(List<ProductPriceDTO> productPriceDTOList) throws IOException {
        try {
            BulkRequest request = new BulkRequest();
            for(ProductPriceDTO productPriceDTO : productPriceDTOList) {
                try{
                    //build upserts query
                    IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, String.valueOf(productPriceDTO.getProductId()))
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
                                    .field("currency", Constants.CURRENCY_SG)
                                    .field("current_price", DejaUtils.getPriceToSGDPrice(productPriceDTO.getCurrency(),productPriceDTO.getCurrentPrice()))
                                    .field("deli_color_top2_score_1", 0)
                                    .field("deli_color_top2_score_2",0)
                                    .field("deli_color_top2_score_id_1", 0)
                                    .field("deli_color_top2_score_id_2", 0)
                                    .field("description", "")
                                    .field("detail_description", "")
                                    .field("discount_percentage", DejaUtils.calculateDiscountPercentage(productPriceDTO.getCurrentPrice(),productPriceDTO.getOriginalPrice()))
                                    .field("height", 0)
                                    .field("image_url", 0)
                                    .field("is_delete", true)
                                    .field("is_discount", productPriceDTO.getCurrentPrice()<productPriceDTO.getOriginalPrice())
                                    .field("is_new_arrival", false)
                                    .field("is_ocb", false)
                                    .field("is_purchasable", false)
                                    .field("is_recommend", true)
                                    .field("last_update_time", 0)
                                    .field("length", 0)
                                    .field("merchant_id", 0)
                                    .field("neckline", 0)
                                    .field("original_price", DejaUtils.getPriceToSGDPrice(productPriceDTO.getCurrency(),productPriceDTO.getOriginalPrice()))
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
                                    .field("validate_status", true)
                                    .endObject());
                    UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, String.valueOf(productPriceDTO.getProductId()))
                            .doc(jsonBuilder()
                                    .startObject()
                                    .field("currency", Constants.CURRENCY_SG)
                                    .field("current_price", DejaUtils.getPriceToSGDPrice(productPriceDTO.getCurrency(), productPriceDTO.getCurrentPrice()))
                                    .field("original_price", DejaUtils.getPriceToSGDPrice(productPriceDTO.getCurrency(), productPriceDTO.getOriginalPrice()))
                                    .field("is_discount", productPriceDTO.getCurrentPrice()<productPriceDTO.getOriginalPrice())
                                    .field("discount_percentage", DejaUtils.calculateDiscountPercentage(productPriceDTO.getCurrentPrice(),productPriceDTO.getOriginalPrice()))
                                    .endObject())
                            .upsert(indexRequest);

                    request.add(updateRequest);

                }
                catch(Exception e){
                    log.error("product price format error, skip it {}", e);
                }
            }
            log.info("product bulk api size is {}", productPriceDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }catch(Exception e){
            log.error("upsert product error",e);
        }
    }

    private void validatePrice(List<ProductPriceDTO> productPriceDTOList){
        Map<Long, String> productPriceValidateMap = new HashMap<>();
        for(ProductPriceDTO productPriceDTO : productPriceDTOList){
            try{
                productPriceValidateMap.put(productPriceDTO.getProductId(), DejaUtils.validatePrice(productPriceDTO).toString());
            }catch(Exception e){
                log.error("product price validate format error {}", e);
                productPriceValidateMap.put(Constants.ERROR_FORMAT_PRODUCT_ID, DejaUtils.validatePrice(productPriceDTO).toString());
            }
        }

        if(!productPriceValidateMap.isEmpty()) {
            log.info(" product price validate sent to kafka map {}", productPriceValidateMap);
            send(productPriceValidateMap, Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE);
            log.info("send kafka done");
        }
        productPriceValidateMap.clear();

    }

    public void send(Map<Long, String> statusMap, String topic){
        Properties kafkaProps =  new Properties();
        kafkaProps.put("bootstrap.servers", this.bootstrapServer);

        LongSerializer longSerializer = new LongSerializer();
        KafkaProducer producer = new KafkaProducer(kafkaProps,longSerializer, new StringSerializer());

        List<ProducerRecord> recordList = new ArrayList<>();
        //mock customer behaviors
        for(Map.Entry entry : statusMap.entrySet()) {
            recordList.add(new ProducerRecord(topic, entry.getKey(), entry.getValue()));
        }
        for(ProducerRecord record : recordList){
            producer.send(record, (RecordMetadata r, Exception e) -> {
                if(e != null){
                    e.printStackTrace();
                }
            });
        }
        producer.close();
    }

}
