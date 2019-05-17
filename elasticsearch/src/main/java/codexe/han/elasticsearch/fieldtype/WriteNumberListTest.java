package codexe.han.elasticsearch.fieldtype;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 如果直接写入一个整数数组，filed类型会被自动设置成number, 我们可以对该字段使用term agg, 从而分析该数字字段的分布
 *
 * GET test_number_list/tags/_search
 * {
 *   "query":{
 *     "term":{
 *       "num":1
 *     }
 *   },
 *   "aggs":{
 *     "num_agg":{
 *       "terms":{
 *         "field":"num"
 *       }
 *     }
 *   }
 * }
 *
 * In Elasticsearch, there is no dedicated array type. Any field can contain zero or more values by default, however, all values in the array must be of the same datatype. For instance:
 *
 * an array of strings: [ "one", "two" ]
 * an array of integers: [ 1, 2 ]
 * an array of arrays: [ 1, [ 2, 3 ]] which is the equivalent of [ 1, 2, 3 ]
 * an array of objects: [ { "name": "Mary", "age": 12 }, { "name": "John", "age": 10 }]
 */
public class WriteNumberListTest {
    public static void main(String[] args) {
        String ip = "172.28.2.22";
        String schema = "http";
        Integer port = 9200;

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));

        List<Integer> list = Arrays.asList(4,5);
        IndexRequest indexRequest = new IndexRequest("test_number_list", "tags").id("4");
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("num", list.toArray());
            }
            builder.endObject();
            indexRequest.source(builder);

            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
