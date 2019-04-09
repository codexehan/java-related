package deja.fashion.datapipeline.test;

import deja.fashion.datapipeline.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestKeepAlive {
    public static void main(String[] args) {
        String ip = "10.60.6.147";

        int port = 9200;
        String schema = "http";

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip,port,schema)));
        int maxRetries = 3;
        boolean retry = true;
        int count = 0;
        try {
            while (retry) {
                try {
                    GetRequest getRequest = new GetRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, "0");
                    GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
                    retry = false;
                } catch (IOException e) {
                    log.error("keep alive, communicating with es error, retry... {}", count);
                    if (++count == maxRetries) {
                        log.error("keep alive, communicating with es error");
                        throw e;
                    }
                } catch (Exception e1) {
                    log.error("keep alive, other exception ", e1);
                    retry = false;
                }
            }
        }catch(Exception e){
            log.error("test keep alive error", e);
        }
    }
}
