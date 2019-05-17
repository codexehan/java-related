package codexe.han.elasticsearch.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Scroll search
 */
@Slf4j
public class RefreshGarmentsFiled {
    public static void main(String[] args) {
        String ip = "172.28.10.67";
        String schema = "http";
        Integer port = 9200;

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("deja_street_for_analysis");
        searchRequest.types("tags");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(5000);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("is_recommended", true));
        boolQuery.must(QueryBuilders.termQuery("is_delete", false));
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);

        int page = 1;
        try{
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            while (searchHits != null && searchHits.length > 0) {
                BulkRequest bulkRequest = new BulkRequest();
                List<Map<String, Object>> sourceMapList = new ArrayList<>();
                MultiSearchRequest multiSearchRequest = new MultiSearchRequest();

                for (SearchHit hit : searchHits) {
                    Map<String, Object> sourceMap = hit.getSourceAsMap();
                    sourceMapList.add(sourceMap);
                    Integer streetForAnalysisId = (Integer) sourceMap.get("street_for_analysis_id");

                    SearchRequest searchRequest1 = new SearchRequest("deja_street_item");
                    searchRequest1.types("tags");
                    SearchSourceBuilder searchSourceBuilder1 = new SearchSourceBuilder();
                    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("is_purchasable_count").gte(3));
                    boolQueryBuilder.must(QueryBuilders.termQuery("street_for_analysis_id", streetForAnalysisId));
                    searchSourceBuilder1.query(boolQueryBuilder);
                    searchRequest1.source(searchSourceBuilder1);

                    multiSearchRequest.add(searchRequest1);
                    /*SearchResponse searchResponse1 = client.search(searchRequest1, RequestOptions.DEFAULT);
                    List<Long> garmentIdList = new ArrayList<>();
                    for(SearchHit hit1 : searchResponse1.getHits().getHits()){
                        if(hit1.getSourceAsMap().keySet().contains("garment_id")) {
                            garmentIdList.add(((Integer) hit1.getSourceAsMap().get("garment_id")).longValue());
                        }
                    }

                    if(!garmentIdList.isEmpty()){
                        UpdateRequest updateRequest = new UpdateRequest("deja_street_for_analysis", "tags", hit.getId());
                        XContentBuilder builder = XContentFactory.jsonBuilder();
                        builder.startObject();
                        {
                            builder.field("garments", garmentIdList);
                        }
                        builder.endObject();
                        updateRequest.doc(builder);
                   //     client.update(updateRequest, RequestOptions.DEFAULT);
                        bulkRequest.add(updateRequest);
                    }*/

                }

                MultiSearchResponse multiSearchResponse = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);

                int i=0;
                for (SearchHit hit : searchHits) {

                    SearchResponse searchResponse1 = multiSearchResponse.getResponses()[i].getResponse();
                    i++;
                    List<Long> garmentIdList = new ArrayList<>();
                    for(SearchHit hit1 : searchResponse1.getHits().getHits()){
                        if(hit1.getSourceAsMap().keySet().contains("garment_id")) {
                            garmentIdList.add(((Integer) hit1.getSourceAsMap().get("garment_id")).longValue());
                        }
                    }

                    if(!garmentIdList.isEmpty()){
                        UpdateRequest updateRequest = new UpdateRequest("deja_street_for_analysis", "tags", hit.getId());
                        XContentBuilder builder = XContentFactory.jsonBuilder();
                        builder.startObject();
                        {
                            builder.field("garments", garmentIdList);
                        }
                        builder.endObject();
                        updateRequest.doc(builder);
                   //     client.update(updateRequest, RequestOptions.DEFAULT);
                        bulkRequest.add(updateRequest);
                    }

                }
                //bulk update
                if(bulkRequest.numberOfActions()!=0) {
                    BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                    if (bulkResponse.hasFailures()) {
                        log.error("failure reason is {}", bulkResponse.buildFailureMessage());
                    } else {
                        log.info("no failures");
                    }
                }
                log.info("update page {} done", page++);
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }

            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            log.info("clear scroll response {}", succeeded);
            client.close();
        }catch(Exception e){
            log.error("update es error", e);
        }
    }
}
