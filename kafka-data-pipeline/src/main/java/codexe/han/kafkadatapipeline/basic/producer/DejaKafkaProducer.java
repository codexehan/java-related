package codexe.han.kafkadatapipeline.basic.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class DejaKafkaProducer implements Closeable {

    private KafkaProducer kafkaProducer;

    public DejaKafkaProducer(KafkaProducer kafkaProducer){
        this.kafkaProducer = kafkaProducer;
    }

    public static KafkaProdcuerBuilder builder(){
        return new KafkaProdcuerBuilder();
    }

    public static class KafkaProdcuerBuilder{

        private String bootstrapServers;

        public KafkaProdcuerBuilder bootstrapServers(String bootstrapServers){
            this.bootstrapServers = bootstrapServers;
            return this;
        }

        public DejaKafkaProducer build(){
            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);//default acks mode is 1
            return new DejaKafkaProducer(new KafkaProducer(props, new StringSerializer(), new StringSerializer()));
        }
    }

    public void send(String topic, String key, String value) {
        log.info("send one kafka message");
        ProducerRecord record = new ProducerRecord(topic, key, value);
        this.kafkaProducer.send(record, (RecordMetadata r, Exception e)->{
            if(e!=null){
                log.error("kafka send message error", e);
                // send message error to slack
            }
            else{
                log.info("kafka send message successfully {}", r.offset());
            }
        });
    }

    @Override
    public void close() throws IOException {
        log.info("kafka connection closed");
        this.kafkaProducer.close();
    }

    public static void main(String[] args) {
        try(DejaKafkaProducer dejaKafkaProducer = DejaKafkaProducer.builder().bootstrapServers("172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092").build()){
            for(int i = 0; i<10; i++){
                dejaKafkaProducer.send("send_test", UUID.randomUUID().toString(), UUID.randomUUID().toString());
            }
        } catch (IOException e) {
            log.error("send kafka message error ",e);
        }
    }
}
