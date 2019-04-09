package deja.fashion.datapipeline.consumer.product;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.common.DejaUtils;
import deja.fashion.datapipeline.consumer.DataConsumer;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.dto.product.ProductImageDTO;
import deja.fashion.datapipeline.dto.product.SizeGuideTableDTO;
import deja.fashion.datapipeline.entity.PipelineKafkaOffset;
import deja.fashion.datapipeline.repository.PipelineKafkaOffsetRepository;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import deja.fashion.datapipeline.service.Impl.PipelineKafkaOffsetServiceImpl;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KeyValue;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class ProductConsumer extends DataConsumer {


    public ProductConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }


    @Override
    public void consume(){
        log.info("---------------Consume Product Start------------------");
        List<ProductDTO> productDTOList = new ArrayList<>();
        openElasticClient();
        while (true) {
            long nextOffset = -1;
            ConsumerRecords<String, ProductDTO> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, ProductDTO> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", record.timestamp(), record.offset(), record.key(), record.value());
                nextOffset = record.offset()+1;
                if(record.value()!=null && record.value().getProductId()!=null) {
                    productDTOList.add(record.value());
                }
                else{
                    log.error("product consumer format has error");
                }
            }
            if (!productDTOList.isEmpty()) {
                try {
                    keepAlive();

                    log.info("---------------Write Product ES Start------------------");
                    toProductEs(productDTOList);
                    log.info("---------------Write Product ES End------------------");

                    log.info("---------------Write Product Image ES Start------------------");
                    toImageEs(productDTOList);
                    log.info("---------------Write Product Image ES End------------------");

                    log.info("---------------Write Product Detail ES Start------------------");
                    toProductDetailEs(productDTOList);
                    log.info("---------------Write Product Detail ES End------------------");

                    log.info("---------------Validate Product And Send To Validate Topic Start------------------");
                    validateProductAndPrice(productDTOList);
                    log.info("---------------Validate Product And Send To Validate Topic End------------------");
                }catch(Exception e){
                    log.error("product io exception {}", e);
                }

            }
            productDTOList.clear();
     //       consumer.commitSync();
            if(nextOffset>0){
                log.info("product next offset {}", nextOffset);
                this.pipelineKafkaOffsetService.updateOffset(pipelineKafkaOffset.getId(), nextOffset);
            }
        }
    }


    @Retryable(value = {IOException.class})
    private void toProductEs(List<ProductDTO> productDTOList) throws IOException {
        /**
         *  useless
         * .field("all_size", false)
         * .field("in_stock", )
         * .field("validate_message", )
         * .field("validate_status", )
         */
        try {
            BulkRequest request = new BulkRequest();
            for(ProductDTO productDTO : productDTOList) {
                try{
                    ProductImageDTO defaultImage = ProductImageDTO.builder().imageUrl("").width(0).height(0).build();
                    if(productDTO.getProductImageList()!=null && !productDTO.getProductImageList().isEmpty()){
                        defaultImage = productDTO.getProductImageList().get(0);
                    }
                    if(productDTO.getCurrentPrice()==null) productDTO.setCurrentPrice(0L);
                    if(productDTO.getOriginalPrice()==null) productDTO.setOriginalPrice(0L);

                    XContentBuilder builderIndex = extractProductAddField(productDTO);
                    XContentBuilder builderUpdate = extractProductAddField(productDTO);

                    //build upserts query
                    IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, String.valueOf(productDTO.getProductId()))
                            .source(builderIndex
                                    .field("autotag_version", productDTO.getAutotagVersion())
                                    .field("brand_id", productDTO.getBrandId())
                                    .field("brand_name", productDTO.getBrandName())
                                    .field("brand_url", productDTO.getBrandUrl())
                                    .field("breadcrumb", productDTO.getBreadcrumb())
                                    .field("category", productDTO.getCategory())
                                    .field("cloth_id", productDTO.getClothId())
                                    .field("color", productDTO.getColor())
                                    .field("color_and_pattern",DejaUtils.getColorAndPattern(productDTO.getColor(), productDTO.getPattern()))
                                    .field("create_time", productDTO.getCreateTime())
                                    .field("currency", Constants.CURRENCY_SG)
                                    .field("current_price", DejaUtils.getPriceToSGDPrice(productDTO.getCurrency(),productDTO.getCurrentPrice()))
                                    .field("deli_color_top2_score_1", productDTO.getExtendTag().getDeliColorTop2().get(0).getScore())
                                    .field("deli_color_top2_score_2", productDTO.getExtendTag().getDeliColorTop2().get(1).getScore())
                                    .field("deli_color_top2_score_id_1", productDTO.getExtendTag().getDeliColorTop2().get(0).getId())
                                    .field("deli_color_top2_score_id_2", productDTO.getExtendTag().getDeliColorTop2().get(1).getId())
                                    .field("description", productDTO.getDescription())
                                    .field("detail_description", productDTO.getDetailDescription())
                                    .field("discount_percentage", DejaUtils.calculateDiscountPercentage(productDTO.getCurrentPrice(), productDTO.getOriginalPrice()))
                                    .field("height", defaultImage.getHeight())
                                    .field("image_url", defaultImage.getImageUrl())
                                    .field("is_delete", productDTO.isDelete())
                                    .field("is_discount", productDTO.getOriginalPrice() > productDTO.getCurrentPrice())
                                    .field("is_new_arrival", DejaUtils.isNewArrival(productDTO.getCreateTime()))
                                    .field("is_ocb", productDTO.isOcb())
                                    .field("is_purchasable", false)
                                    .field("is_recommend", true)
                                    .field("last_update_time", productDTO.getLastUpdateTime())
                                    .field("length", productDTO.getLength())
                                    .field("merchant_id", productDTO.getMerchantId())
                                    .field("neckline", productDTO.getNeckline())
                                    .field("original_price", DejaUtils.getPriceToSGDPrice(productDTO.getCurrency(),productDTO.getOriginalPrice()))
                                    .field("pattern", productDTO.getPattern())
                                    .field("popularity", 0)
                                    .field("product_code", productDTO.getProductCode())
                                    .field("product_color", productDTO.getProductColor())
                                    .field("product_group_id", DejaUtils.filterGroupId(productDTO.getGroupId()))
                                    .field("product_id", productDTO.getProductId())
                                    .field("product_name", productDTO.getName())
                                    .field("recommend_reason", "")
                                    .field("site_mark", productDTO.getSiteMark())
                                    .field("size_guide_table", productDTO.getSizeGuideTable())
                                    .field("sleeve_length", productDTO.getSleeveLength())
                                    .field("subcategory", productDTO.getSubCategory())
                                    .field("weight", 10000)
                                    .field("width", defaultImage.getWidth())
                                    .field("validate_status", true)//cuz kuan add this field...
                                    .endObject());
                    UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, String.valueOf(productDTO.getProductId()))
                            .doc(builderUpdate
                                    //        .field("all_size", false)
                                    .field("autotag_version", productDTO.getAutotagVersion())
                                    .field("brand_id", productDTO.getBrandId())
                                    .field("brand_name", productDTO.getBrandName())
                                    .field("brand_url", productDTO.getBrandUrl())
                                    .field("breadcrumb", productDTO.getBreadcrumb())
                                    .field("category", productDTO.getCategory())
                                    .field("cloth_id", productDTO.getClothId())
                                    .field("color", productDTO.getColor())
                                    .field("color_and_pattern",DejaUtils.getColorAndPattern(productDTO.getColor(), productDTO.getPattern()))
                                    .field("create_time", productDTO.getCreateTime())
                                    .field("currency", Constants.CURRENCY_SG)
                                    .field("current_price", DejaUtils.getPriceToSGDPrice(productDTO.getCurrency(),productDTO.getCurrentPrice()))
                                    .field("deli_color_top2_score_1", productDTO.getExtendTag().getDeliColorTop2().get(0).getScore())
                                    .field("deli_color_top2_score_2", productDTO.getExtendTag().getDeliColorTop2().get(1).getScore())
                                    .field("deli_color_top2_score_id_1", productDTO.getExtendTag().getDeliColorTop2().get(0).getId())
                                    .field("deli_color_top2_score_id_2", productDTO.getExtendTag().getDeliColorTop2().get(1).getId())
                                    .field("description", productDTO.getDescription())
                                    .field("detail_description", productDTO.getDetailDescription())
                                    .field("discount_percentage", DejaUtils.calculateDiscountPercentage(productDTO.getCurrentPrice(), productDTO.getOriginalPrice()))
                                    .field("height", defaultImage.getHeight())
                                    .field("image_url", defaultImage.getImageUrl())
                                    .field("is_delete", productDTO.isDelete())
                                    .field("is_discount", productDTO.getOriginalPrice() > productDTO.getCurrentPrice())
                                    .field("is_new_arrival", DejaUtils.isNewArrival(productDTO.getCreateTime()))
                                    .field("is_ocb", productDTO.isOcb())
                                    //           .field("is_purchasable", false)//purchasable field will be updated by stream pipeline
                                    .field("is_recommend", true)
                                    .field("last_update_time", productDTO.getLastUpdateTime())
                                    .field("length", productDTO.getLength())
                                    .field("merchant_id", productDTO.getMerchantId())
                                    .field("neckline", productDTO.getNeckline())
                                    .field("original_price", DejaUtils.getPriceToSGDPrice(productDTO.getCurrency(),productDTO.getOriginalPrice()))
                                    .field("pattern", productDTO.getPattern())
                                    //         .field("popularity", 0)
                                    .field("product_code", productDTO.getProductCode())
                                    .field("product_color", productDTO.getProductColor())
                                    .field("product_group_id", DejaUtils.filterGroupId(productDTO.getGroupId()))
                                    .field("product_id", productDTO.getProductId())
                                    .field("product_name", productDTO.getName())
                                    .field("recommend_reason", "")
                                    .field("site_mark", productDTO.getSiteMark())
                                    .field("size_guide_table", productDTO.getSizeGuideTable())
                                    .field("sleeve_length", productDTO.getSleeveLength())
                                    .field("subcategory", productDTO.getSubCategory())
                                    //          .field("weight", 10000)
                                    .field("width", defaultImage.getWidth())
                                    .field("validate_status", true)//cuz kuan add this field...
                                    .endObject())
                            .upsert(indexRequest);

                    request.add(updateRequest);
                }catch(Exception e){
                    log.error("product format has error, skip it {}",e);
                }
            }
            log.info("product bulk api size is {}", productDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert product error",e);
        }
    }

    @Retryable(value = {IOException.class})
    private void toImageEs(List<ProductDTO> productDTOList) throws IOException {
        try {
            int imageSize = 0;
            BulkRequest request = new BulkRequest();
            for(ProductDTO productDTO : productDTOList) {
                for (int i = 0; i < productDTO.getProductImageList().size(); i++, imageSize++) {
                    try {
                        ProductImageDTO productImageDTO = productDTO.getProductImageList().get(i);
                        String imageId = String.valueOf(productDTO.getProductId()) + "_" + productImageDTO.getPhash();
                        //build upserts query
                        IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT_IMAGE, Constants.ES_TYPE_DEJA_PRODUCT_IMAGE, imageId)
                                .source(jsonBuilder()
                                        .startObject()
                                        .field("hash_id", imageId)
                                        .field("height", productImageDTO.getHeight())
                                        .field("image_url", productImageDTO.getImageUrl())
                                        .field("is_default", i == 0)
                                        .field("product_id", productDTO.getProductId())
                                        .field("ranking", (i + 1) * 10)
                                        .field("width", productImageDTO.getWidth())
                                        .field("attribute", productImageDTO.getAttribute())
                                        .endObject());
                        request.add(indexRequest);
                    } catch (Exception e1) {
                        log.error("product image format error {}", e1);
                    }
                }
            }
            log.info("product image bulk api size is {}", imageSize);
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product image bulk update fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert product error",e);
        }
    }

    @Retryable(value = {IOException.class})
    private void toProductDetailEs(List<ProductDTO> productDTOList) throws IOException {
        try {
            DejaJsonSerde sizeGuideTableSerde = DejaJsonSerde.<SizeGuideTableDTO>builder().clazz(SizeGuideTableDTO.class).build();
            BulkRequest request = new BulkRequest();
            for(ProductDTO productDTO : productDTOList) {
                try{
                    //build upserts query
                    IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_PRODUCT_DETAIL, Constants.ES_TYPE_DEJA_PRODUCT_DETAIL, String.valueOf(productDTO.getProductId()))
                            .source(jsonBuilder()
                                    .startObject()
                                    .field("description", productDTO.getDescription())
                                    .field("detail_description",productDTO.getDetailDescription())
                                    .field("last_modified_date", productDTO.getLastUpdateTime())
                                    .field("product_id", productDTO.getProductId())
                                    .field("size_guide_description", productDTO.getSizeGuideDescription())
                                    .field("size_guide_table", productDTO.getSizeGuideTable())
                                    .endObject());
                    request.add(indexRequest);
                }catch(Exception e){
                    log.error("product detail format error {}", e);
                }
            }
            log.info("product detail bulk api size is {}", productDTOList.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("product detail bulk update fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert product error",e);
        }
    }

    private XContentBuilder extractProductAddField(ProductDTO productDTO) throws Exception{
        XContentBuilder builder = jsonBuilder().startObject();
        if(productDTO.getOtherIntFields()!=null) {
            for (Map.Entry<String, Long> entry :productDTO.getOtherIntFields().entrySet()) {
                log.info("other integer field of product is {} : {}", entry.getKey(), entry.getValue());
                builder.field(entry.getKey(), entry.getValue());
            }
        }
        if(productDTO.getOtherStrFields()!=null) {
            for (Map.Entry<String, String> entry :productDTO.getOtherStrFields().entrySet()) {
                log.info("other string field of product is {} : {}", entry.getKey(), entry.getValue());
                builder.field(entry.getKey(), entry.getValue());
            }
        }
        if(productDTO.getOtherFloatFields()!=null) {
            for (Map.Entry<String, Float> entry :productDTO.getOtherFloatFields().entrySet()) {
                log.info("other float field of product is {} : {}", entry.getKey(), entry.getValue());
                builder.field(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }

    private void validateProductAndPrice(List<ProductDTO> productDTOList){
        Map<Long, String> productValidateMap = new HashMap<>();
        Map<Long, String> productPriceValidateMap = new HashMap<>();
        for(ProductDTO productDTO : productDTOList){
            try {
                productValidateMap.put(Long.valueOf(productDTO.getProductId()), DejaUtils.validateProduct(productDTO).toString());
            }catch(Exception e){
                log.error("product validate format error {}", e);
                productValidateMap.put(Constants.ERROR_FORMAT_PRODUCT_ID, DejaUtils.validateProduct(productDTO).toString());
            }
            //validate price
            try {
                productPriceValidateMap.put(Long.valueOf(productDTO.getProductId()), DejaUtils.validatePrice(ProductPriceDTO.builder()
                        .productId(productDTO.getProductId())
                        .currency(productDTO.getCurrency())
                        .currentPrice(productDTO.getCurrentPrice())
                        .originalPrice(productDTO.getOriginalPrice())
                        .isOffline(false).build()).toString());
            }catch(Exception e){
                log.error("product price validate format error {}", e);
                productPriceValidateMap.put(Constants.ERROR_FORMAT_PRODUCT_ID, "false");
            }
        }

        if(!productValidateMap.isEmpty()) {
            log.info(" product validate sent to kafka map {}", productValidateMap);
            send(productValidateMap, Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE);
            log.info("send kafka done");
        }
        productValidateMap.clear();

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
