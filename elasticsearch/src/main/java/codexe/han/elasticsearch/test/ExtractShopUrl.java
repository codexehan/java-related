package codexe.han.elasticsearch.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExtractShopUrl {
    public static void main(String[] args) {

       /* String clickUrl1 = "https://invol.co/aff_m?offer_id=1232&aff_id=107789&source=feed&url=https%3A//zilingo.com/en-sg/product/details/PRO5098318083%3Fcolor%3Dblack";
        //clickUrl  = URLDecoder.decode(clickUrl, "UTF-8");
        log.info("click url is {}", clickUrl1);
        String brandUrl1 = clickUrl1.substring(clickUrl1.lastIndexOf("https"),clickUrl1.length());
        try {
            log.info("brand url is {}",URLDecoder.decode(brandUrl1,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/


       // String ip = "10.60.6.147";
        String ip = "10.60.12.174";
        String schema = "http";
        Integer port = 9200;

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));
        final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
        SearchRequest searchRequest = new SearchRequest("deja_products");
        searchRequest.types("tags");
        searchRequest.scroll(scroll);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(5000);
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("is_purchasable", true));
        boolQuery.must(QueryBuilders.termsQuery("brand_id", Arrays.asList(28218,28217)));
        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);

        int page = 1;
        try{
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            String scrollId = searchResponse.getScrollId();
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            while (searchHits != null && searchHits.length > 0) {
                BulkRequest bulkRequest = new BulkRequest();

                for (SearchHit hit : searchHits) {
                    Map<String, Object> sourceMap = hit.getSourceAsMap();
                    String clickUrl = (String) sourceMap.get("click_url");
                    //clickUrl  = URLDecoder.decode(clickUrl, "UTF-8");
                    log.info("click url is {}", clickUrl);
                    String brandUrl = clickUrl.substring(clickUrl.lastIndexOf("https"),clickUrl.length());
                    log.info("brand url is {}",brandUrl);
                    Map<String,String> brandUrlMap = new HashMap<>();
                    brandUrlMap.put("brand_url",URLDecoder.decode(brandUrl,"UTF-8"));
                    UpdateRequest updateRequest = new UpdateRequest("deja_products", "tags", hit.getId());
                    updateRequest.doc(brandUrlMap);
                    bulkRequest.add(updateRequest);
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
