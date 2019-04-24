package codexe.han.kafkadatapipeline.middleware;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class KafkaStreamMiddleware {

    public KafkaStreamMiddleware(String bootstrapServer, String groupId, String topic, Deserializer keyJsonSerde, Deserializer valueJsonSerde){

    }
}
