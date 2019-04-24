package codexe.han.kafkadatapipeline.consumer;

import codexe.han.kafkadatapipeline.client.ElasticsearchClient;
import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.DataBaseDTO;
import codexe.han.kafkadatapipeline.entity.PipelineKafkaOffset;
import codexe.han.kafkadatapipeline.service.Impl.PipelineKafkaOffsetServiceImpl;
import codexe.han.kafkadatapipeline.service.PipelineKafkaOffsetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.Deserializer;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import scala.collection.immutable.Stream;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Slf4j
public class DataConsumer<T extends DataBaseDTO> {

    public volatile boolean exit = false;

    protected KafkaConsumer<Long, T> consumer;

    protected ElasticsearchClient elasticsearchClient;

    protected String index;

    protected String type;

    private String ip;

    private int port;

    private String schema;

    protected String bootstrapServer;

    protected String topic;

    protected PipelineKafkaOffsetService pipelineKafkaOffsetService;

    protected PipelineKafkaOffset pipelineKafkaOffset;


    public DataConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde, PipelineKafkaOffsetService pipelineKafkaOffsetService){
        this.bootstrapServer = bootstrapServer;
        this.topic = topic;
        this.pipelineKafkaOffsetService = pipelineKafkaOffsetService;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put("enable.auto.commit", "false");
        //props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
   //    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "");
        //props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "");
 //   props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        /*props.put("enable.auto.commit", "true");*/
       /* props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024 * 1024 * 2);
        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000 * 5);*/
     //   props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerCount);
        this.consumer = new KafkaConsumer<Long, T>(props, keyJsonSerde, valueJsonSerde);
  //      consumer.subscribe(Arrays.asList(topic));
        this.pipelineKafkaOffset = this.pipelineKafkaOffsetService.getKafkaOffsetByTopicName(topic);
        Collection<TopicPartition> topicPartitionCollection = new ArrayList<>();
        topicPartitionCollection.add(new TopicPartition(pipelineKafkaOffset.getTopic(), pipelineKafkaOffset.getPartition()));
        this.consumer.assign(topicPartitionCollection);
        this.consumer.seek(new TopicPartition(pipelineKafkaOffset.getTopic(), pipelineKafkaOffset.getPartition()),pipelineKafkaOffset.getOffset());


    }
    public DataConsumer(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde ){
        this.bootstrapServer = bootstrapServer;
        this.topic = topic;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put("enable.auto.commit", "false");
        this.consumer = new KafkaConsumer<Long, T>(props, keyJsonSerde, valueJsonSerde);
        consumer.subscribe(Arrays.asList(topic));
    }

    public void initElasticSearch(String ip, int port, String schema, String index, String type){
        this.index = index;
        this.type = type;
        this.ip = ip;
        this.port = port;
        this.schema = schema;

    }

    public void openElasticClient(){
        this.elasticsearchClient = ElasticsearchClient.build(this.ip,this.port, this.schema);
    }

    public void closeElasticClient() {
        try {
            this.elasticsearchClient.getClient().close();
        }catch(Exception e){
            log.error("close elasticsearch client error {}", e);
        }
    }

    public void keepAlive() throws IOException{
        int maxRetries = 3;
        boolean retry = true;
        int count = 0;
        while(retry){
            try {
                GetRequest getRequest = new GetRequest(Constants.ES_INDEX_DEJA_PRODUCT, Constants.ES_TYPE_DEJA_PRODUCT, "0");
                this.elasticsearchClient.getClient().get(getRequest, RequestOptions.DEFAULT);
                retry = false;
            } catch (IOException e) {
                log.error("keep alive, communicating with es error, retry... {}", count);
                if(++count == maxRetries){
                    log.error("keep alive, communicating with es error");
                    throw e;
                }
            } catch(Exception e1){
                log.error("keep alive, other exception ", e1);
                retry =false;
            }
        }
    }

    public void consume(){
        while (true) {
            log.info("---------------Consume Product Price Start------------------");
            ConsumerRecords<Long, T> records = consumer.poll(Duration.ofSeconds(1));
            List<T> dataList = new ArrayList<>();
            for (ConsumerRecord<Long, T> record : records) {
                log.info("time = {}, offset = {}, key = {}, value = {}", new Date(record.timestamp()), record.offset(), record.key(), record.value());dataList.add(record.value());
            }
            log.info("---------------Consume Product Price Start------------------");

            log.info("---------------Write ES Start------------------");
            toEs(dataList);
            log.info("---------------Write ES End------------------");

        }
    }


    private void toEs(List<T> dataList){
        try {
            BulkRequest request = new BulkRequest();
            for(T dataDTO : dataList) {
                //build upserts query
                XContentBuilder xContentBuilder = jsonBuilder().startObject();
                for(String esField : dataDTO.getEsFieldList()){
                    xContentBuilder.field(esField, dataDTO.get(esField));
                }
                xContentBuilder.endObject();
                IndexRequest indexRequest = new IndexRequest(this.index, this.type, dataDTO.getEsId())
                        .source(xContentBuilder);
                request.add(indexRequest);
            }
            log.info("bulk api size is {}", dataList.size());
            if(request.numberOfActions() > 0) {
                BulkResponse bulkResponse = this.elasticsearchClient.getClient().bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    log.info("bulk upsert fail : {}", bulkResponse.buildFailureMessage());
                }
            }
        }catch(Exception e){
            log.error("upsert error",e);
        }
    }


}
