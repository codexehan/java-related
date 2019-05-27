package codexe.han.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class CopyIndex {
    public static void main(String[] args) {
        String toIP = "172.28.10.55";
        String fromIP = "xx.xxx.xx.xxx";

        int port = 9200;
        String schema = "http";
        RestHighLevelClient to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        RestHighLevelClient from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
    //    copyInfluencer(from, to, "deja_influencer", "deja_influencer");
    //    copyInfluencer(from, to, "deja_street_item", "deja_street_item");
     //   copyPsRelation(from,to, "deja_ps_relation", "deja_ps_relation");
        /*copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");
        copyInfluencer(from, to, "deja_street_item", "deja_street_item");*/


 //       copyInfluencer(from, to, "deja_products", "deja_products");

         /*to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
         from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_account", "deja_account");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_brand", "deja_brand");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_celebrity_hot_account_batch", "deja_celebrity_hot_account_batch");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_celebrity_hot_street_batch ", "deja_celebrity_hot_street_batch");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_event_tracking", "deja_event_tracking");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_influencer_race", "deja_influencer_race");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_influencer_region", "deja_influencer_region");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_influencer_similarity_relation", "deja_influencer_similarity_relation");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_influencer_style", "deja_influencer_style");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_influencer_style_relation", "deja_influencer_style_relation");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_product_tag", "deja_product_tag");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_cashback", "deja_products_cashback");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_detail", "deja_products_detail");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_image", "deja_products_image");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_image_cashback", "deja_products_image_cashback");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_inventory", "deja_products_inventory");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_products_inventory_cashback", "deja_products_inventory_cashback");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_ss_relation", "deja_ss_relation");

        to = new RestHighLevelClient(RestClient.builder(new HttpHost(toIP, port, schema)));
        from = new RestHighLevelClient(RestClient.builder(new HttpHost(fromIP, port, schema)));
        copyInfluencer(from, to, "deja_tag", "deja_tag");*/



        copyInfluencer(from, to, "deja_street_for_analysis", "deja_street_for_analysis");




        try {
            from.close();
            to.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get all index

    }
    public static void copyInfluencer(RestHighLevelClient from, RestHighLevelClient to, String fromIndex, String toIndex){
        try{
            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest(fromIndex);
            searchRequest.types("tags");
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(5000);
            searchRequest.source(searchSourceBuilder);

            int page = 1;
            SearchResponse searchResponse = from.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            while (searchHits != null && searchHits.length > 0) {
                BulkRequest bulkRequest = new BulkRequest();

                for (SearchHit hit : searchHits) {
                    Map<String, Object> sourceMap = hit.getSourceAsMap();

                    IndexRequest indexRequest = new IndexRequest(toIndex, "tags", hit.getId()).source(sourceMap);
                    bulkRequest.add(indexRequest);
                }

                BulkResponse bulkResponse = to.bulk(bulkRequest, RequestOptions.DEFAULT);
                if(bulkResponse.hasFailures()){
                    log.error("failure reason is {}", bulkResponse.buildFailureMessage());
                }
                else{
                    log.info("no failures");
                }
                log.info("update page {} done", page++);
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = from.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }

            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = from.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            log.info("clear scroll response {}", succeeded);
            to.close();
            from.close();
        }catch(Exception e){
            log.error("copy error ",e);
        }

    }
    public static void copyPsRelation(RestHighLevelClient from, RestHighLevelClient to, String fromIndex, String toIndex){
        try{
            final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
            SearchRequest searchRequest = new SearchRequest(fromIndex);
            searchRequest.types("tags");
            searchRequest.scroll(scroll);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(5000);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("is_purchasable", true));
            searchSourceBuilder.query(boolQueryBuilder);
            searchRequest.source(searchSourceBuilder);

            int page = 1;
            SearchResponse searchResponse = from.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            while (searchHits != null && searchHits.length > 0) {
                BulkRequest bulkRequest = new BulkRequest();

                for (SearchHit hit : searchHits) {
                    Map<String, Object> sourceMap = hit.getSourceAsMap();

                    IndexRequest indexRequest = new IndexRequest(toIndex, "tags", hit.getId()).source(sourceMap);
                    bulkRequest.add(indexRequest);
                }

                BulkResponse bulkResponse = to.bulk(bulkRequest, RequestOptions.DEFAULT);
                if(bulkResponse.hasFailures()){
                    log.error("failure reason is {}", bulkResponse.buildFailureMessage());
                }
                else{
                    log.info("no failures");
                }
                log.info("update page {} done", page++);
                SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
                scrollRequest.scroll(scroll);
                searchResponse = from.scroll(scrollRequest, RequestOptions.DEFAULT);
                scrollId = searchResponse.getScrollId();
                searchHits = searchResponse.getHits().getHits();
            }

            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(scrollId);
            ClearScrollResponse clearScrollResponse = from.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            boolean succeeded = clearScrollResponse.isSucceeded();
            log.info("clear scroll response {}", succeeded);
            to.close();
            from.close();
        }catch(Exception e){
            log.error("copy error ",e);
        }

    }
}
