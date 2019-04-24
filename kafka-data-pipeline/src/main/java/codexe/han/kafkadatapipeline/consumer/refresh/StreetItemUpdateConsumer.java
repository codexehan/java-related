package codexe.han.kafkadatapipeline.consumer.refresh;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.consumer.DataConsumer;
import codexe.han.kafkadatapipeline.entity.PipelineKafkaOffset;
import codexe.han.kafkadatapipeline.service.Impl.PipelineKafkaOffsetServiceImpl;
import codexe.han.kafkadatapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class StreetItemUpdateConsumer extends DataConsumer {


    public StreetItemUpdateConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde, pipelineKafkaOffsetService);
    }
    public StreetItemUpdateConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde ){
        super(bootstrapServer, groupId, topic, keyJsonSerde, valueJsonSerde);
    }

    public void consume(){
        log.info("---------------Consume Street Item Update Start------------------");
        Set<Long> streetItemSet = new HashSet<>();
        openElasticClient();
        while (true) {
            long nextOffset = -1;
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", record.timestamp(), record.offset(), record.key(), record.value());
                nextOffset = record.offset()+1;
                try {
                    streetItemSet.add(Long.valueOf(record.value()));
                }catch(Exception e){
                    log.error("street item id format error", e);
                }
                if(streetItemSet.size() >= Constants.BULK_PAGE_SIZE) {
                    log.info("---------------Write Ps Relation ES Start------------------");
                    updateCountField(streetItemSet);
                    log.info("---------------Write Ps Relation ES End------------------");
                }
            }
            if(!streetItemSet.isEmpty()) {
                log.info("---------------Write Ps Relation ES Start------------------");
                updateCountField(streetItemSet);
                log.info("---------------Write Ps Relation ES End------------------");
            }
            streetItemSet.clear();
            if(nextOffset>0){
                log.info("update next offset {}", nextOffset);
                this.pipelineKafkaOffsetService.updateOffset(pipelineKafkaOffset.getId(),nextOffset);
            //    closeElasticClient();
            }
        }
    }

    public void updateCountField(Set<Long> streetItemSet){

        try {
            keepAlive();

            log.info("update street item count...");
            updateStreetItemCount(streetItemSet);

            log.info("update street for analysis count...");
            updateStreetForAnalysisRecommend(streetItemSet);

            streetItemSet.clear();
        } catch (Exception e) {
            log.error("street item update consumer erorr", e);
        }
    }

    @Retryable(value = {IOException.class})
    public Map<String, Long> countPurchasableStreetItem(Set<Long> streetItemSet) throws IOException {
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
        try{
            //get street item whose count is not 0
            searchRequest.types(Constants.ES_TYPE_DEJA_PS_RELATION);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("street_item_id", streetItemSet));
            boolQueryBuilder.must(QueryBuilders.termsQuery("is_purchasable", true));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_item_count").field("street_item_id").size(Constants.MAX_COUNT_SIZE);

            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);
            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            Map<String, Long> countMap = extractCount(aggregationBuilder, searchResponse);
            log.info("street item purchasable count maps {}", countMap);

            return countMap;

        }
        catch(IOException ie){
            throw ie;
        }
        catch (Exception e){
            log.error("getResult error, json : {}", searchRequest.source());
        }
        return null;
    }
    public void updateStreetItemCount(Set<Long> streetItemSet){
        try{
            Map<String, Long> countMap = countPurchasableStreetItem(streetItemSet);//get purchasable count in street item set, others will be 0
            //0 street item will be filltered, so need to be added
            fillZeroCount(countMap, streetItemSet);
            updateStreetItemByUpdateRequest(countMap);
            countMap.clear();
        }catch (Exception e){
            log.error("update Street Item count io error {}", e);
        }
    }

    public void fillZeroCount(Map<String, Long> countMap, Set<Long> streetItemSet){

        //update to street item which is purchasable
        Set<Long> nonZeroCountStreetItemSet = countMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet());
        log.info("original streetItemSet size {}", streetItemSet.size());
        Set<Long> streetItemSetClone = new HashSet<>(streetItemSet);
        streetItemSetClone.removeAll(nonZeroCountStreetItemSet);
        log.info("zeroCountStreetItemSet size {}", streetItemSetClone.size());
        //fill zero map to count map
        for(Long streetItemId : streetItemSetClone){
            countMap.put(streetItemId.toString(),0L);
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

    @Retryable(value = {IOException.class})
    public void updateStreetItemByUpdateRequest(Map<String, Long> countMap) throws IOException {
        try {
            //paging
            BulkRequest request = new BulkRequest();
            for(Map.Entry<String, Long> entry : countMap.entrySet()) {
                //build upserts query
               /* IndexRequest indexRequest = new IndexRequest(Constants.ES_INDEX_DEJA_STREET_ITEM, Constants.ES_TYPE_DEJA_STREET_ITEM, entry.getKey())
                        .source(jsonBuilder()
                                .startObject()
                                .field("street_item_id", Long.valueOf(entry.getKey()))
                                .field("is_purchasable_count", entry.getValue())
                                .endObject());
                UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_STREET_ITEM, Constants.ES_TYPE_DEJA_STREET_ITEM, entry.getKey())
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_purchasable_count", entry.getValue())
                                .endObject())
                        .upsert(indexRequest);*/
                UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_STREET_ITEM, Constants.ES_TYPE_DEJA_STREET_ITEM, entry.getKey())
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_purchasable_count", entry.getValue())
                                .endObject());


                request.add(updateRequest);

            }
            request.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            log.info(" bulk api size is {}", countMap.size());
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("street item bulk upsert fail : {}, may cause street for analysis update error!!!!", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert ps relation error",e);
        }
    }

    public void updateStreetForAnalysisRecommend(Set<Long> streetItemSet){

        try{
            //get all street snap id to update
            Map<String, Long> countMap = getStreetForAnalysisId(streetItemSet);
            //get street snap id to update to recommended true
            Set<Long> streetForAnalysisIdSetAll = countMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet());
            log.info("check street for analysis id set {}", streetForAnalysisIdSetAll);

            Map<String, Long> streetForAnalysisRecommendTrueMap = getStreetForAnalysisRecommendedTrue(streetForAnalysisIdSetAll);

            //get street snap id to update to recommended false
            Set<Long> updateToRecommended = streetForAnalysisRecommendTrueMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet());
            log.info("street snap recommended set is {}", updateToRecommended);
            streetForAnalysisIdSetAll.removeAll(updateToRecommended);
            log.info("street snap not recommended set is {}", streetForAnalysisIdSetAll);

            log.info("update recommended to true or false");
            updateStreetSnapByUpdateRequest(updateToRecommended, streetForAnalysisIdSetAll);

            countMap.clear();
            streetForAnalysisRecommendTrueMap.clear();
            updateToRecommended.clear();
            streetForAnalysisIdSetAll.clear();

        }catch (Exception e){
            log.error("updateStreetForAnalysisRecommend error {}", e);
        }
    }

    @Retryable(value = {IOException.class})
    private Map<String, Long> getStreetForAnalysisId(Set<Long> streetItemSet) throws IOException {
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_STREET_ITEM);
        try {
            //get all street snap id to update
            searchRequest.types(Constants.ES_TYPE_DEJA_STREET_ITEM);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("street_item_id", streetItemSet));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_for_analysis_count").field("street_for_analysis_id").size(Constants.MAX_COUNT_SIZE);
            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);

            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            return extractCount(aggregationBuilder, searchResponse);
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("getStreetForAnalysisId {}", e);
        }
        return null;
    }

    @Retryable(value = {IOException.class})
    private Map<String, Long> getStreetForAnalysisRecommendedTrue(Set<Long> streetForAnalysisIdSet) throws IOException {
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_STREET_ITEM);
        try {
            //get all street snap id to update
            log.info("street snap need to update set is {}", streetForAnalysisIdSet);
            searchRequest.types(Constants.ES_TYPE_DEJA_STREET_ITEM);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("street_for_analysis_id", streetForAnalysisIdSet));
            boolQueryBuilder.must(QueryBuilders.rangeQuery("is_purchasable_count").gte(Constants.PURCHASABLE_COUNT_THRESHOLD));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_for_analysis_count").field("street_for_analysis_id").size(Constants.MAX_COUNT_SIZE);
            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);

            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            return extractCount(aggregationBuilder, searchResponse);
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("getStreetForAnalysisId {}", e);
        }
        return null;
    }

    @Retryable(value = {IOException.class})
    private void updateStreetSnapByUpdateRequest(Set<Long> recommendedSet, Set<Long> unrecommendedSet) throws IOException {
        log.info("recommend set is {}, not recommend set is {}", recommendedSet, unrecommendedSet);
        try {
            BulkRequest request = new BulkRequest();
            for(Long recommendedItemId : recommendedSet) {
                //build upserts query
                UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_STREET_FOR_ANALYSIS, Constants.ES_TYPE_DEJA_STREET_FOR_ANALYSIS, String.valueOf(recommendedItemId))
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_recommended", true)
                                .endObject());

                request.add(updateRequest);

            }
            for(Long unrecommendedItemId : unrecommendedSet) {
                //build upserts query
                UpdateRequest updateRequest = new UpdateRequest(Constants.ES_INDEX_DEJA_STREET_FOR_ANALYSIS, Constants.ES_TYPE_DEJA_STREET_FOR_ANALYSIS, String.valueOf(unrecommendedItemId))
                        .doc(jsonBuilder()
                                .startObject()
                                .field("is_recommended", false)
                                .endObject());

                request.add(updateRequest);

            }
            log.info(" bulk api size is {}", recommendedSet.size()+unrecommendedSet.size());
            //187246
            BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
            if(bulkResponse.hasFailures()){
                log.info("bulk upsert fail : {}", bulkResponse.buildFailureMessage());
            }
        }
        catch(IOException ie){
            throw ie;
        }
        catch(Exception e){
            log.error("upsert street for analysis error",e);
        }
    }


    /*******************************************Refresh All Count************************************************/
    public void resetCountOfStreetItem(){
        try{
            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            UpdateByQueryRequest request =new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_STREET_ITEM);
            request.setQuery(QueryBuilders.matchAllQuery());
            request.setScript(new Script("ctx._source.is_purchasable_count = 0"));

            request.setTimeout(TimeValue.timeValueMinutes(30));

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);
            if(bulkResponse.isTimedOut()){
                log.error("bulk update time out");
            }
            else{
                if(!bulkResponse.getBulkFailures().isEmpty()){
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }

        }catch (Exception e){
            log.error("getResult error, json : {}", e);
        }
    }
    public void refreshAllStreetItemCount(){
        //reset count field to 0
        resetCountOfStreetItem();

        //get all purchasable product
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
        try{
            searchRequest.types(Constants.ES_TYPE_DEJA_PS_RELATION);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("is_purchasable", true));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_item_count").field("street_item_id").size(Constants.MAX_COUNT_SIZE);

            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);
            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            Map<String, Long> countMap = extractCount(aggregationBuilder, searchResponse);

            //329335
            updateStreetItemByUpdateRequest(countMap);

        }catch (Exception e){
            log.error("getResult error, json : {}", searchRequest.source());
        }
    }

    public void resetRecommendOfStreetForAnalysis(){
        try{
            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            UpdateByQueryRequest request =new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_STREET_FOR_ANALYSIS);
            request.setQuery(QueryBuilders.matchAllQuery());
            request.setScript(new Script("ctx._source.is_recommended = false"));
            request.setTimeout(TimeValue.timeValueMinutes(30));

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);
            if(bulkResponse.isTimedOut()){
                log.error("bulk update time out");
            }
            else{
                if(!bulkResponse.getBulkFailures().isEmpty()){
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }

        }catch (Exception e){
            log.error("getResult error, json : {}", e);
        }
    }

    public void refreshAllStreetForAnalysisRecommend(){
        //reset recommend field to false
        resetRecommendOfStreetForAnalysis();

        //get all purchasable product
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_STREET_ITEM);
        try{
            searchRequest.types(Constants.ES_TYPE_DEJA_STREET_ITEM);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.rangeQuery("is_purchasable_count").gte(Constants.PURCHASABLE_COUNT_THRESHOLD));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("street_for_analysis__recommended_count").field("street_for_analysis_id").size(Constants.MAX_COUNT_SIZE);

            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);
            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            Map<String, Long> countMap = extractCount(aggregationBuilder, searchResponse);

            updateStreetSnapByUpdateRequest(countMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet()), new HashSet<>());

        }catch (Exception e){
            log.error("getResult error, json : {}", searchRequest.source());
        }
    }

    /*******************************************Refresh All Ps Relation************************************************/
    public void refreshPsRelationPurchasableTrueField(){
        Set<Long> purchasableCountSet  = getAllPurchasableProduct();
        updatePruchasableFieldTrueToPs(purchasableCountSet);
    }
    public void refreshPsRelationPurchasableFalseField(){
        Set<Long> purchasableCountSet  = getAllPurchasableProduct();
        updatePruchasableFieldFalseToPs(purchasableCountSet);
    }
    public void updatePruchasableFieldTrueToPs(Set<Long> purchasableProductSet){
        //get all purchasable product
        try{
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id", purchasableProductSet));

            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            UpdateByQueryRequest request =new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
            request.setQuery(boolQueryBuilder);
            request.setScript(new Script(ScriptType.INLINE, "painless","ctx._source.is_purchasable = true", Collections.emptyMap()));

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);
            if(bulkResponse.isTimedOut()){
                log.error("bulk update time out");
            }
            else{
                if(!bulkResponse.getBulkFailures().isEmpty()){
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }

        }catch (Exception e){
            log.error("getResult error, json : {}", e);
        }
    }
    public void updatePruchasableFieldFalseToPs(Set<Long> purchasableProductSet){
        //get all purchasable product
        try{
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.mustNot(QueryBuilders.termsQuery("product_id", purchasableProductSet));

            //reference https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.5/java-rest-high-document-update-by-query.html
            UpdateByQueryRequest request =new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_PS_RELATION);
            request.setQuery(boolQueryBuilder);
            request.setScript(new Script("ctx._source.is_purchasable = false"));

            BulkByScrollResponse bulkResponse = this.elasticsearchClient.getClient().updateByQuery(request, RequestOptions.DEFAULT);
            if(bulkResponse.isTimedOut()){
                log.error("bulk update time out");
            }
            else{
                if(!bulkResponse.getBulkFailures().isEmpty()){
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }

        }catch (Exception e){
            log.error("getResult error, json : {}", e);
        }
    }

    public Set<Long> getAllPurchasableProduct(){
        //get all purchasable product
        SearchRequest searchRequest = new SearchRequest(Constants.ES_INDEX_DEJA_PRODUCT);
        try{
            searchRequest.types(Constants.ES_TYPE_DEJA_PRODUCT);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("is_purchasable", true));

            AggregationBuilder aggregationBuilder = AggregationBuilders.terms("product_count").field("product_id").size(Constants.MAX_COUNT_SIZE);

            sourceBuilder.query(boolQueryBuilder).aggregation(aggregationBuilder);
            sourceBuilder.size(0);
            sourceBuilder.timeout(new TimeValue(30, TimeUnit.MINUTES));

            searchRequest.source(sourceBuilder);
            SearchResponse searchResponse = this.elasticsearchClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

            Map<String, Long> countMap = extractCount(aggregationBuilder, searchResponse);

            return countMap.keySet().stream().map(key->Long.valueOf(key)).collect(Collectors.toSet());

        }catch (Exception e){
            log.error("getResult error, json : {}", searchRequest.source());
        }
        return null;
    }

}
