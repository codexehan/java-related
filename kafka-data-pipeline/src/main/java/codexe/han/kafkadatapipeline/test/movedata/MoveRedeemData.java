package deja.fashion.datapipeline.test.movedata;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.*;

@Slf4j
public class MoveRedeemData {
    public static void main(String[] args) {

        Long[] idArray = new Long[]{
                5471442L,
                5471498L,
                5471499L,
                5471500L,
                5471503L,
                5471504L,
                5471505L,
                5471443L,
                5471506L,
                5471449L,
                5471447L,
                5471448L,
                5471451L,
                5471453L,
                5471454L,
                5471455L,
                5471456L,
                5471457L,
                5471458L,
                5471462L,
                5471463L,
                5471464L,
                5471468L,
                5471469L,
                5471470L,
                5471474L,
                5471475L,
                5471481L,
                5471484L,
                5471485L,
                5471487L,
                5471488L,
                5471489L,
                5471491L,
                5471492L,
                5471495L,
                5471497L,
                5471444L,
                5471446L,
                5471452L,
                5471459L,
                5471496L,
                5471471L,
                5471472L,
                5471473L,
                5471477L,
                5471486L,
                5430375L,
                5430387L,
                5430600L,
                5430390L,
                5430582L,
                5452859L,
                5457629L,
                5431989L,
                5431929L,
                5425329L,
                5440851L,
                5430113L,
                5457186L,
                5458118L,
                5453974L,
                5438519L,
                5456429L,
                5483585L,
                5438445L,
                5438502L,
                5452767L,
                5485599L,
                5485559L,
                5478631L,
                5485592L,
                5478613L,
                5477692L,
                5477674L,
                5477675L,
                5478611L,
                5478629L,
                5480313L,
                5480000L,
                5487270L,
                5487387L,
                5479486L,
                5487283L,
                5483424L,
                5480032L,
                5483289L,
                5482321L,
                5479286L,
                5551263L,
                5479727L,
                5480202L,
                5482847L,
                5486657L,
                5477279L,
                5447621L,
                5449477L,
                5446544L,
                5484931L,
                5450576L,
                5494583L,
                5449158L,
                5446647L,
                5448442L,
                5505429L,
                5476982L,
                5448561L,
                5448842L,
                5553363L,
                5468409L,
                5437733L,
                5463952L,
                5470208L,
                5470174L,
                5454330L,
                5438598L,
                5438521L,
                5458522L,
                5461274L,
                5492315L,
                5459914L,
                5461037L,
                5492490L,
                5458538L,
                5504886L,
                5557073L,
                5456942L,
                5557839L,
                5481102L,
                5481098L,
                5469631L,
                5551865L,
                5472953L,
                5422608L,
                5422606L,
                5415232L,
                5417701L,
                5415512L,
                5415364L,
                5408164L
        };

        String ip = "172.28.10.67";


        int port = 9200;
        String schema = "http";
        Set<Long> productIdSet = new HashSet<>(Arrays.asList(idArray));


  //      deleteIndex(ip, port, schema, "deja_products_cashback");
        copyProduct(ip, port, schema, productIdSet, "deja_products", "deja_products_cashback");
  //      copy(ip, port, schema, productIdSet, "deja_products_detail", "deja_products_detail_cashback");
  //      copy(ip, port, schema, productIdSet, "deja_products_image", "deja_products_image_cashback");
    }

    public static void copy(String ip, int port, String schema, Set<Long> productIdSet, String fromIndex, String toIndex){
        log.info("product id set size is {}", productIdSet.size());
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip,port,schema)));
        try{
            SearchRequest searchRequest = new SearchRequest(fromIndex);
            searchRequest.types("tags");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id",productIdSet));

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);

            sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
            sourceBuilder.size(1000);
            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            log.info("search result size is {}", response.getHits().getTotalHits());

            Map<String, Map<String, Object>> searchRes = new HashMap<>();
            BulkRequest bulkRequest = new BulkRequest();
            for(SearchHit hit :response.getHits().getHits()) {
                IndexRequest indexRequest = new IndexRequest(toIndex, "tags", hit.getId());
                indexRequest.type("tags");
                indexRequest.source(hit.getSourceAsMap());
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("insertion has error {}", bulkResponse.hasFailures());

            client.close();
            //write to es
        }catch(Exception e){
            log.error("get info from es error");
        }
    }
    public static void copyProduct(String ip, int port, String schema, Set<Long> productIdSet, String fromIndex, String toIndex){
        log.info("product id set size is {}", productIdSet.size());
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip,port,schema)));
        try{
            SearchRequest searchRequest = new SearchRequest(fromIndex);
            searchRequest.types("tags");
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("product_id",productIdSet));

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(boolQueryBuilder);

            sourceBuilder.timeout(TimeValue.timeValueMinutes(1L));
            sourceBuilder.size(1000);
            searchRequest.source(sourceBuilder);

            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            log.info("search result size is {}", response.getHits().getTotalHits());

            Map<String, Map<String, Object>> searchRes = new HashMap<>();
            BulkRequest bulkRequest = new BulkRequest();
            for(SearchHit hit :response.getHits().getHits()) {
                IndexRequest indexRequest = new IndexRequest(toIndex, "tags", hit.getId());
                indexRequest.type("tags");
                hit.getSourceAsMap().put("is_ocb", true);
                hit.getSourceAsMap().put("is_purchasable", true);
                hit.getSourceAsMap().put("is_delete", false);
                hit.getSourceAsMap().put("validate_status", true);
                hit.getSourceAsMap().put("current_price", hit.getSourceAsMap().get("original_price"));

                if(((String)hit.getSourceAsMap().get("brand_name")).toLowerCase().contains("asos")) {
                    hit.getSourceAsMap().put("brand_name", "High Street Collection");
                    hit.getSourceAsMap().put("product_name", ((String)hit.getSourceAsMap().get("product_name")).toLowerCase().replace("asos",""));
                }

                indexRequest.source(hit.getSourceAsMap());
                bulkRequest.add(indexRequest);
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("insertion has error {}", bulkResponse.hasFailures());

            client.close();
            //write to es
        }catch(Exception e){
            log.error("get info from es error");
        }
    }
    public static void deleteIndex(String ip, int port, String schema, String index){
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip,port,schema)));
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            log.info("delete result {}", deleteIndexResponse.isAcknowledged());
            client.close();
        }catch(Exception e){
            log.error("delete index error ", e);
        }
    }
}

