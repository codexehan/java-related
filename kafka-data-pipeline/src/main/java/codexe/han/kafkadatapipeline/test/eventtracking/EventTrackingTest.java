package codexe.han.kafkadatapipeline.test.eventtracking;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.consumer.eventtracking.EventTrackingConsumer;
import codexe.han.kafkadatapipeline.dto.eventtracking.EventTrackingEvent;
import codexe.han.kafkadatapipeline.serde.DejaJsonSerde;
import codexe.han.kafkadatapipeline.test.useless.InfluencerRaceJsonSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;

@Slf4j
public class EventTrackingTest {
    public static void main(String[] args) {
      //  String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
  //      String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";

        String ip = "172.28.10.59";
        int port = 9200;
        String schema = "http";
        String index = Constants.ES_INDEX_DEJA_EVENT_TRACKING;
        String type = "tags";

        String groupId = "data-pipeline-dev-test";
        DejaJsonSerde dejaJsonSerde = DejaJsonSerde.<EventTrackingEvent>builder().clazz(EventTrackingEvent.class).build();
        EventTrackingConsumer eventTrackingConsumer = new EventTrackingConsumer(bootstrapServer, groupId, Constants.KAFKA_TOPIC_EVENT_TRACKING, new StringDeserializer(), dejaJsonSerde);
        eventTrackingConsumer.initElasticSearch(ip, port, schema, index, type);
        eventTrackingConsumer.consume();
    }
}
