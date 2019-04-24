package codexe.han.kafkadatapipeline.test.mockdata;

import codexe.han.kafkadatapipeline.client.ElasticsearchClient;
import codexe.han.kafkadatapipeline.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class MockStreetItemTest {
    public static void main(String[] args) {
        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        String index = Constants.ES_INDEX_DEJA_STREET_ITEM;
        String type = "tags";
        ElasticsearchClient elasticsearchClient = ElasticsearchClient.build(ip, port, schema);
        try {
            BulkRequest request = new BulkRequest();
            //build upserts query
            IndexRequest indexRequest = new IndexRequest(index, type, "11221")
                    .source(jsonBuilder()
                            .startObject()
                            .field("create_time", 1538741226)
                            .field("is_eligible",true)
                            .field("is_purchasable_count", 0)
                            .field("last_update_time", 1538741226)
                            .field("magic_point_hsv", "{\"h\":131,\"s\":147,\"v\":55}")
                            .field("magic_point_position", "{\"x\":536,\"y\":591}")
                            .field("parsed_area", 44)
                            .field("parsed_path", "/product/street_parsed/cc/0e/cc0ef065982335ea17e5c9b6ce0d466c.png")
                            .field("review_status", 0)
                            .field("street_for_analysis_id", 370450)
                            .field("street_item_id", 11221)
                            .field("sub_category", 200)
                            .endObject());
            request.add(indexRequest);

            BulkResponse bulkResponse = elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
        }catch(Exception e){
            log.error("upsert product error",e);
        }
    }

}
