package codexe.han.kafkadatapipeline.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;


@Slf4j
public class ElasticsearchClient {

    private RestHighLevelClient client;

    private ElasticsearchClient(String ip, int port, String scheme){
        client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip,port,scheme)));

    }

    private final int DEFAULT_SIZE = 10000;

    private final int DEFAULT_TIMEOUT = 600;

    public static ElasticsearchClient build(String ip,int port,String scheme){
        return new ElasticsearchClient(ip,port,scheme);
    }

    public RestHighLevelClient getClient(){
        return client;
    }
}
