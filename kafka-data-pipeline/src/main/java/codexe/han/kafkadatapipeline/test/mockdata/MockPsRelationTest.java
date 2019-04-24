package codexe.han.kafkadatapipeline.test.mockdata;

import codexe.han.kafkadatapipeline.client.ElasticsearchClient;
import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import codexe.han.kafkadatapipeline.dto.ProductPriceDTO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class MockPsRelationTest {
    public static void main(String[] args) {
        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        String index = Constants.ES_INDEX_DEJA_PS_RELATION;
        String type = "tags";
        ElasticsearchClient elasticsearchClient = ElasticsearchClient.build(ip, port, schema);
        try {
            BulkRequest request = new BulkRequest();
            //build upserts query
            IndexRequest indexRequest = new IndexRequest(index, type, "1_1")
                    .source(jsonBuilder()
                            .startObject()
                            .field("distance", 0)
                            .field("is_purchasable",false)
                            .field("last_update_time", 7845456)
                            .field("product_id", 11111L)
                            .field("ps_relation_id", 4556)
                            .field("review_status", 0)
                            .field("street_analysis_id", 78521)
                            .field("street_item_id", 11221)
                            .field("sub_category", 193)
                            .endObject());
            request.add(indexRequest);
            log.info("index request version is {}", indexRequest.version());

            BulkResponse bulkResponse = elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
        }catch(Exception e){
            log.error("upsert product error",e);
        }
    }
}
