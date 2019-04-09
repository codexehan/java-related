package deja.fashion.datapipeline.consumer.product;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.consumer.DataConsumer;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.dto.product.ProductStatusChangeDTO;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class ProductStatusChangeConsumer extends DataConsumer {
    public ProductStatusChangeConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService) {
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }

    @Override
    public void consume(){
        log.info("---------------Consume Product Status Change Start------------------");
        Map<Long, Boolean> productUpdateMap = new HashMap<>();
        openElasticClient();
        while (true) {
            long nextOffset = -1;
            ConsumerRecords<Long, ProductStatusChangeDTO> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Long, ProductStatusChangeDTO> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", record.timestamp(), record.offset(), record.key(), record.value());
                nextOffset = record.offset()+1;
                if(record.value()!=null) {
                    /*if (record.value().isNeedUpdate()) {
                        productUpdateMap.put(record.key(), record.value().isPurchasable());
                    }*/
                    productUpdateMap.put(record.key(), record.value().isPurchasable());


                    //paging
                    if (productUpdateMap.size() >= Constants.BULK_PAGE_SIZE) {
                        updateStatus(productUpdateMap);
                    }
                }
                else{
                    log.error("product status change consumer format error");
                }
            }
            if(!productUpdateMap.isEmpty()){
                updateStatus(productUpdateMap);
            }
            productUpdateMap.clear();
            if(nextOffset>0){
                log.info("update next offset {}", nextOffset);
                this.pipelineKafkaOffsetService.updateOffset(pipelineKafkaOffset.getId(),nextOffset);
            //    closeElasticClient();
            }
        }
    }
    public void updateStatus(Map<Long, Boolean> productUpdateMap){
        try {
            keepAlive();

            log.info("product status need update {}", productUpdateMap);
            log.info("---------------Write Product Purchasable Start------------------");
            updateProductStatus(productUpdateMap);
            log.info("---------------Write Product Purchasable End------------------");

            log.info("---------------Write Ps Purchasable Start------------------");
            //two group : purchasable group and not purchasable group
            Set<Long> purchasableProduct = new HashSet<>();
            Set<Long> notPurchasableProduct = new HashSet<>();
            for (Map.Entry<Long, Boolean> entry : productUpdateMap.entrySet()) {
                if (entry.getValue()) {
                    purchasableProduct.add(entry.getKey());
                } else {
                    notPurchasableProduct.add(entry.getKey());
                }
            }

            updatePsRelation(purchasableProduct, notPurchasableProduct);
            log.info("---------------Write Ps Purchasable End------------------");

            log.info("---------------Write Street Item To Kafka Start------------------");
            toKafka(productUpdateMap);
            log.info("---------------Write Street Item To Kafka End------------------");

            productUpdateMap.clear();
        }catch(Exception e){
            log.error("update product purchasable status io error {}", e);
        }
    }
    @Retryable(value = {IOException.class})
    public void updateProductStatus(Map<Long, Boolean> productUpdateMap) throws IOException {
        try {
            BulkRequest request = new BulkRequest();
            for(Map.Entry entry : productUpdateMap.entrySet()) {

                UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, String.valueOf(entry.getKey()))
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_purchasable", entry.getValue())
                                .endObject());

                request.add(updateRequest);

            }
            log.info("product bulk api size is {}", productUpdateMap.size());
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
    public void updatePsRelation(Set<Long>purchasableProduct, Set<Long> notPurchasableProduct){
        try{
            if(!purchasableProduct.isEmpty()) {
                updatePurchasableTruePsRelation(purchasableProduct);
            }
            if(!notPurchasableProduct.isEmpty()) {
                updatePurchasableFalsePsRelation(notPurchasableProduct);
            }
        }catch (Exception e){
            log.error("getResult error, json : {}", e);
        }
    }

    @Retryable(value = {IOException.class})
    private void updatePurchasableTruePsRelation(Set<Long> purchasableProduct) throws IOException {
        try {
            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            log.info("----------------Update Purchsable True Ps Relation---------------------");
            UpdateByQueryRequest request = new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id", purchasableProduct));
            request.setQuery(boolQueryBuilder);
            request.setScript(new Script("ctx._source.is_purchasable = true"));

            request.setTimeout(TimeValue.timeValueMinutes(30));

            request.setRefresh(true);

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);

            if (bulkResponse.isTimedOut()) {
                log.error("bulk update time out");
            } else {
                if (!bulkResponse.getBulkFailures().isEmpty()) {
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("getResult error, json : {}", e);
        }
    }
    @Retryable(value = {IOException.class})
    private void updatePurchasableFalsePsRelation(Set<Long> notPurchasableProduct) throws IOException {
        try {
            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            log.info("----------------Update Purchsable True Ps Relation---------------------");
            UpdateByQueryRequest request = new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id", notPurchasableProduct));
            request.setQuery(boolQueryBuilder);
            request.setScript(new Script("ctx._source.is_purchasable = false"));

            request.setTimeout(TimeValue.timeValueMinutes(30));

            request.setRefresh(true);

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);
            if (bulkResponse.isTimedOut()) {
                log.error("bulk update time out");
            } else {
                if (!bulkResponse.getBulkFailures().isEmpty()) {
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("getResult error, json : {}", e);
        }
    }

    public void toKafka(Map<Long, Boolean> productUpdateMap){
        //get all street item id
        //get all purchasable product
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
        try{
            searchRequest.types(Constants.ES_TYPE_DEJA_PS_RELATION);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id", productUpdateMap.keySet()));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_item_count").field("street_item_id").size(Constants.MAX_COUNT_SIZE);

            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);
            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            Map<String, Long> countMap = extractCount(aggregationBuilder, searchResponse);

            log.info("get street item sent to kafka id set {}", countMap);
            send(countMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet()));
            log.info("send kafka done");

        }catch (IOException e){
            log.error("getResult error, json : {}", searchRequest.source());
        }
    }
    public Map<String, Long> extractCount( AggregationBuilder countAggregation, SearchResponse searchResponse){
        Aggregations aggregations = searchResponse.getAggregations();
        String countField =countAggregation.getName();
        while(countAggregation.getSubAggregations()!=null && !countAggregation.getSubAggregations().isEmpty()) {//count must be located in the last agg which is reasonable
            countAggregation = (AggregationBuilder) countAggregation.getSubAggregations().toArray()[countAggregation.getSubAggregations().size()-1];
            aggregations = ((ParsedFilter)aggregations.get(countField)).getAggregations();
            countField = countAggregation.getName();
        }
        log.info("count field is {}", countField);
        Terms counts = aggregations.get(countField);
        return ((ParsedLongTerms) counts).getBuckets().stream().collect(Collectors.toMap(t -> t.getKey().toString(), t -> t.getDocCount()));
    }

    public void send(Set<Long> streetItemSet){
        Properties kafkaProps =  new Properties();
        kafkaProps.put("bootstrap.servers", this.bootstrapServer);

        LongSerializer longSerializer = new LongSerializer();
        KafkaProducer producer = new KafkaProducer(kafkaProps, new StringSerializer(), new StringSerializer());

        List<ProducerRecord> recordList = new ArrayList<>();
        //mock customer behaviors
        for(Long streetItemId : streetItemSet) {
            recordList.add(new ProducerRecord(Constants.KAFKA_TOPIC_STREET_ITEM_REFRESH, streetItemId.toString(), streetItemId.toString()));
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
