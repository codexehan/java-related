package codexe.han.kafkadatapipeline.middleware.product;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.product.ProductStatusChangeDTO;
import codexe.han.kafkadatapipeline.processor.ProductPurchasableChangeTransformer;
import codexe.han.kafkadatapipeline.serde.DejaJsonSerde;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;
@Slf4j
@Data
@Builder
public class ProductStatusChangeMiddleware {
    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");

        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // create store
        StoreBuilder<KeyValueStore<Long,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE), Serdes.Long(), Serdes.String());
       /* StoreBuilder<KeyValueStore<Long,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.inMemoryKeyValueStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE), Serdes.Long(), Serdes.String());*/
        // register store
        streamsBuilder.addStateStore(keyValueStoreBuilder);

   //     KTable<Long, String> productPurchasableStatusTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS, Consumed.with(Serdes.Long(), Serdes.String()));
        KStream<Long, String> productPurchasableStatusTable = streamsBuilder.stream(Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS, Consumed.with(Serdes.Long(), Serdes.String()));

        DejaJsonSerde dejaJsonSerde = DejaJsonSerde.<ProductStatusChangeDTO>builder().clazz(ProductStatusChangeDTO.class).build();
        productPurchasableStatusTable.transformValues(new ProductPurchasableChangeTransformer(),Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE).to(Constants.KAFKA_TOPIC_PRODUCT_STATUS_CHANGE, Produced.with(Serdes.Long(),dejaJsonSerde));

        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("StreetSnapCountChangeMiddleware running ...");
  //      stream.cleanUp();
        stream.start();

    }
}
