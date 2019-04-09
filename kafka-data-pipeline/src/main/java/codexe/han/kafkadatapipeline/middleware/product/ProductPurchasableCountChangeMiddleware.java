package deja.fashion.datapipeline.middleware.product;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.processor.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;

@Slf4j
@Data
@Builder
public class ProductPurchasableCountChangeMiddleware {

    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");

        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // create store
        StoreBuilder<KeyValueStore<Long,String>> keyValueStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS), Serdes.Long(), Serdes.String());
        // register store
        streamsBuilder.addStateStore(keyValueStoreBuilder);

        KTable<Long, String> productPurchasableStatusTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS, Consumed.with(Serdes.Long(), Serdes.String()));

        productPurchasableStatusTable.transformValues(new ProductPurchasableTransformer()).toStream().to(Constants.KAFKA_STREAM_TOPIC_PRODUCT_PURCHASABLE_COUNT, Produced.with(Serdes.Long(), Serdes.Integer()));

        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("StreetSnapCountChangeMiddleware running ...");
        stream.start();

    }
}
