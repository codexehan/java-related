package codexe.han.kafkadatapipeline.consumer.product;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import codexe.han.kafkadatapipeline.consumer.DataConsumer;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryComboDTO;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryDTO;
import codexe.han.kafkadatapipeline.service.PipelineKafkaOffsetService;
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
public class ProductInventoryConsumer extends DataConsumer {

    public ProductInventoryConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }



    public void consume(){
        log.info("---------------Consume Product Inventory Start------------------");
        List<ProductInventoryComboDTO> productInventoryComboDTOList = new ArrayList<>();
        openElasticClient();
        while (true) {
            long nextOffset = -1;
            ConsumerRecords<String, ProductInventoryComboDTO> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, ProductInventoryComboDTO> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", record.timestamp(), record.offset(), record.key(), record.value());
                nextOffset = record.offset()+1;
                if(record.value()!=null) {
                    productInventoryComboDTOList.add(record.value());
                }
                else{
                    log.error("product inventory format has error");
                }
            }
            if(!productInventoryComboDTOList.isEmpty()) {
                log.info("---------------Write Product Inventory ES Start------------------");
                try {
                    keepAlive();

                    toProductInventoryEs(productInventoryComboDTOList);
                }catch(Exception e){
                    log.error("inventory io exception {}", e);
                }
                log.info("---------------Write Product Inventory ES End------------------");
            }
            productInventoryComboDTOList.clear();
            if(nextOffset>0){
                log.info("update next offset {}", nextOffset);
                this.pipelineKafkaOffsetService.updateOffset(pipelineKafkaOffset.getId(),nextOffset);
         //       closeElasticClient();
            }
        }
    }


    @Retryable(value = {IOException.class})
    private void toProductInventoryEs(List<ProductInventoryComboDTO> productInventoryComboDTOList) throws IOException {
        try {
            BulkRequest request = new BulkRequest();
            for(ProductInventoryComboDTO productInventoryComboDTO : productInventoryComboDTOList) {
                for (ProductInventoryDTO productInventoryDTO : productInventoryComboDTO.getProductInventoryDTOList()) {
                    try {
                        //build upserts query
                        IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT_INVENTORY, Constants.ES_TYPE_DEJA_PRODUCT_INVENTORY, String.valueOf(productInventoryDTO.getInventoryId()))
                                .source(jsonBuilder()
                                        .startObject()
                                        .field("auto_deleted", productInventoryDTO.isAutoDeleted())
                                        .field("create_time", productInventoryDTO.getCreateTime())
                                        .field("inventory_id", productInventoryDTO.getInventoryId())
                                        .field("last_update_time", productInventoryDTO.getLastUpdateTime())
                                        .field("product_id", productInventoryComboDTO.getProductId())
                                        .field("quantity", productInventoryDTO.getQuantity())
                                        .field("size", productInventoryDTO.getSize())
                                        .endObject());
                        request.add(indexRequest);
                    }
                    catch (Exception e) {
                        log.error("product inventory format error, skip it {}", e);
                    }
                }
            }
            log.info("product inventory bulk api size is {}", productInventoryComboDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product inventory bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert product error",e);
        }
    }

}
