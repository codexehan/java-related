package codexe.han.kafkadatapipeline.test;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.consumer.DataConsumer;
import codexe.han.kafkadatapipeline.dto.influencer.InfluencerRaceDTO;
import codexe.han.kafkadatapipeline.test.useless.InfluencerRaceJsonSerde;
import org.apache.kafka.common.serialization.LongDeserializer;

public class ConsumerTest {
    public static void main(String[] args) {
        String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";

        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        String index = Constants.KAFKA_TOPIC_INFLUENCER_RACE;
        String type = "tags";

        String groupId = "data-synchronize-dev";
        DataConsumer<InfluencerRaceDTO> influencerRaceConsumer = new DataConsumer<>(bootstrapServer, groupId, Constants.KAFKA_TOPIC_INFLUENCER_RACE, new LongDeserializer(), new InfluencerRaceJsonSerde<>());
        influencerRaceConsumer.initElasticSearch(ip, port, schema, index, type);
        influencerRaceConsumer.consume();
    }
}
