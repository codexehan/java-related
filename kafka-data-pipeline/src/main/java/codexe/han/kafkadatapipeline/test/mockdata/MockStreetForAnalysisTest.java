package deja.fashion.datapipeline.test.mockdata;

import deja.fashion.datapipeline.client.ElasticsearchClient;
import deja.fashion.datapipeline.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class MockStreetForAnalysisTest {
    public static void main(String[] args) {
        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        String index = Constants.ES_INDEX_DEJA_STREET_FOR_ANALYSIS;
        String type = "tags";
        ElasticsearchClient elasticsearchClient = ElasticsearchClient.build(ip, port, schema);
        try {
            BulkRequest request = new BulkRequest();
            //build upserts query
            IndexRequest indexRequest = new IndexRequest(index, type, "370450")
                    .source(jsonBuilder()
                            .startObject()
                            .field("create_time", 1538741226)
                            .field("delete",false)
                            .field("eligible_item_count", 0)
                            .field("image_height", 1538741226)
                            .field("image_local_path", "/product/street2018/ff/81/ff81708f24e9d34ca0bb70f7094faf2983b4f189.jpg")
                            .field("image_original_url", "https://scontent-sin6-2.cdninstagram.com/vp/0545cfefd6718e88f53cd47d7d1509c5/5BD14040/t51.2885-15/e35/35272884_241989343075601_4829180061330440192_n.jpg")
                            .field("image_width", 1080)
                            .field("influencer_id", 894)
                            .field("is_delete", false)
                            .field("is_recommended", true)
                            .field("last_update_time", 275501)
                            .field("like_count", 200)
                            .field("phash", "30330d2cce10ce667893f190e1303068176a16be9cadd91d38dd673ad806fce1")
                            .field("post_time", 205450)
                            .field("reference", 2424500)
                            .field("review_status", 200)
                            .field("street_for_analysis_id", 370450)
                            .field("street_type", 200)
                            .field("tags", "41, 2")
                            .endObject());
            request.add(indexRequest);

            BulkResponse bulkResponse = elasticsearchClient.getClient().bulk(request,RequestOptions.DEFAULT);
        }catch(Exception e){
            log.error("upsert product error",e);
        }
    }

}
