package deja.fashion.datapipeline.test.es_colorandpattern;

import deja.fashion.datapipeline.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;

@Slf4j
public class RefreshColorAndPattern {
    public static void main(String[] args) {
        String ip = "172.28.10.67";
        int port = 9200;
        String schema = "http";

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));
        try{
            UpdateByQueryRequest request = new UpdateByQueryRequest(Constants.ES_INDEX_DEJA_PRODUCT);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.rangeQuery("pattern").gt(0));
            request.setQuery(boolQueryBuilder);

            request.setScript(new Script("ctx._source.color_and_pattern = ctx._source.pattern"));

            request.setTimeout(TimeValue.timeValueMinutes(30));

            BulkByScrollResponse bulkResponse = client.updateByQuery(request, RequestOptions.DEFAULT);
            if(bulkResponse.isTimedOut()){
                log.error("bulk update time out");
            }
            else{
                if(!bulkResponse.getBulkFailures().isEmpty()){
                    log.error("bulk query by update error {}", bulkResponse.getBulkFailures());
                }
            }
        }catch(Exception e){
            log.error("getResult error, json : {}", e);
        }
    }
}
