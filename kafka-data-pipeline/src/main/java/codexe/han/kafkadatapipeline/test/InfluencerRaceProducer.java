package deja.fashion.datapipeline.test;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.dto.influencer.InfluencerRaceDTO;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;

import java.util.*;

@Slf4j
public class InfluencerRaceProducer {
    public static void main(String[] args) {
        Properties kafkaProps = new Properties();
        kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092");

        LongSerializer longSerializer = new LongSerializer();
    //    InfluencerRaceJsonSerde customJsonSerde = new InfluencerRaceJsonSerde();
        DejaJsonSerde customJsonSerde = DejaJsonSerde.<InfluencerRaceDTO>builder().clazz(InfluencerRaceDTO.class).build();
        KafkaProducer producer = new KafkaProducer(kafkaProps, longSerializer, customJsonSerde);

        List<ProducerRecord> recordList = new ArrayList<>();
        //mock customer behaviors

        InfluencerRaceDTO influencerRaceDTO = InfluencerRaceDTO.builder()
                .id(2000L)
                .name("Occidental")
                .isDelete(false)
                .createTime(1538323200000L)
                .lastUpdateTime(1538928000000L)
                .build();


        Set<Integer> testSet = new HashSet<>(Arrays.asList(new Integer[]{1,2,5}));
        DejaJsonSerde testSerde = DejaJsonSerde.<Set>builder().clazz(Set.class).build();

        log.info("serialized format is {}", new String(testSerde.serialize("",testSet)));
        log.info("deserialized format is {}", testSerde.deserialize("",testSerde.serialize("",testSet)));

        Boolean b = true;
        log.info("boolean to string is {}", b.toString());

        log.info("serialized format is {}", new String(customJsonSerde.serialize("",influencerRaceDTO)));
        log.info("deserialized format is {}", customJsonSerde.deserialize("",customJsonSerde.serialize("",influencerRaceDTO)));

        recordList.add(new ProducerRecord(Constants.KAFKA_TOPIC_INFLUENCER_RACE, influencerRaceDTO.getId(), influencerRaceDTO));


        for(ProducerRecord record : recordList){
            producer.send(record, (RecordMetadata r, Exception e) -> {
                if(e != null){
                    e.printStackTrace();
                }
            });
        }


        producer.close();

    }
}
