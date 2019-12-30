package codexe.han.kafkadatapipeline;


import codexe.han.kafkadatapipeline.流控功能consumer.KafkaConsumerClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class FlowControlKafkaConsumerTest {

    private KafkaConsumerClient kafkaConsumerClient;
    private List<String> topicList;
    private String bootstrapServers;

    public void initKafkaBrokerTopic(){
        this.bootstrapServers = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        this.topicList = Arrays.asList("flow_control_test");
    }

    public void setUpKafkaConsumerClient(){
        //create kafka consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "flow_control_test_group_1");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "flow_control_test_group_member");
        props.put("enable.auto.commit", "false");

        kafkaConsumerClient = new KafkaConsumerClient(props, topicList, 5,10,10);

    }
    public void produceMockData(){
        //create kafka producer and send some mock data
        Properties kafkaProps =  new Properties();
        kafkaProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        //send to dp kafka
        KafkaProducer kafkaProducer = new KafkaProducer(kafkaProps, new StringSerializer(), new StringSerializer());
        //send 1000 messages to flow_control_test topic
        for(String topic : this.topicList) {
            for (Integer i = 0; i < 1000; i++) {
                ProducerRecord producerRecord = new ProducerRecord(topic, i.toString(), i.toString());
                kafkaProducer.send(producerRecord, (RecordMetadata r, Exception e) -> {
                    if(e != null){
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Before
    public void init(){
        initKafkaBrokerTopic();
        setUpKafkaConsumerClient();
   //     produceMockData();
    }

    @Test
    public void testKafkaConsumerClient(){
        kafkaConsumerClient.consume();
    }
}
