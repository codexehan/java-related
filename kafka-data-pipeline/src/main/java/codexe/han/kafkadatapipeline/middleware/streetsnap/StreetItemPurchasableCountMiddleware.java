package codexe.han.kafkadatapipeline.middleware.streetsnap;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.streetsnap.PsRelationDTO;
import codexe.han.kafkadatapipeline.dto.streetsnap.StreetItemDTO;
import codexe.han.kafkadatapipeline.dto.streetsnap.StreetItemUpdateDTO;
import codexe.han.kafkadatapipeline.processor.PsRelationProcessor;
import codexe.han.kafkadatapipeline.processor.StreetItemTransformer;
import codexe.han.kafkadatapipeline.processor.StreetItemPurchasableCountUpdateTransformer;
import codexe.han.kafkadatapipeline.serde.DejaJsonSerde;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.util.Properties;
import java.util.Set;

@Data
@Builder
@Slf4j
public class StreetItemPurchasableCountMiddleware {
    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        /**
         * ps first, then street item
         */
        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        // create store
        DejaJsonSerde pruductStreetItemRelationSerde = DejaJsonSerde.<Set>builder().clazz(Set.class).build();
        StoreBuilder<KeyValueStore<Long, Set<Long>>> productStreetItemRelationStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_PRODUCT_STREET_ITEM_RELATION), Serdes.Long(), pruductStreetItemRelationSerde);


        //DejaJsonSerde streetItemCountDataSerde = DejaJsonSerde.<StreetItemCountDTO>builder().clazz(StreetItemCountDTO.class).build();
        StoreBuilder<KeyValueStore<Long, Integer>> streetItemCountStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_STREET_ITEM_PURCHASABLE_COUNT_STATUS), Serdes.Long(), Serdes.Integer());
        StoreBuilder<KeyValueStore<Long, Long>> streetItemStreetSnapRelationStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_STREET_ITEM_STREETSNAP_RELATION), Serdes.Long(), Serdes.Long());

        DejaJsonSerde streetForAnalysisRecommendedDataSerde = DejaJsonSerde.<Set>builder().clazz(Set.class).build();
        StoreBuilder<KeyValueStore<Long,Set<Long>>> streetForAnalysisRecommendedStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(Constants.LOCAL_STORE_STREET_SNAP_RECOMMENDED_STATUS), Serdes.Long(), streetForAnalysisRecommendedDataSerde);

        // register store
        streamsBuilder.addStateStore(productStreetItemRelationStoreBuilder);
        streamsBuilder.addStateStore(streetItemStreetSnapRelationStoreBuilder);
        streamsBuilder.addStateStore(streetItemCountStoreBuilder);
        streamsBuilder.addStateStore(streetForAnalysisRecommendedStoreBuilder);

        //init p-s s-s relation store
        DejaJsonSerde psRelationJsonSerde = DejaJsonSerde.<PsRelationDTO>builder().clazz(PsRelationDTO.class).build();
        KStream<Long, PsRelationDTO> psRelationStream = streamsBuilder.stream(Constants.KAFKA_TOPIC_PS_RELATION, Consumed.with(Serdes.Long(), psRelationJsonSerde));
        psRelationStream.process(()->new PsRelationProcessor());

        //init street item count store  and street for analysis recommend store
        DejaJsonSerde streetItemJsonSerde = DejaJsonSerde.<StreetItemDTO>builder().clazz(StreetItemDTO.class).build();
        KStream<Long, StreetItemDTO> streetItemStream = streamsBuilder.stream(Constants.KAFKA_TOPIC_STREET_ITEM, Consumed.with(Serdes.Long(), streetItemJsonSerde));
   //     streetItemStream.process(()->new StreetItemTransformer());


        /////////////////////////////////////////////////////////////////////////////////////



        //update product purchasable count in real time
        DejaJsonSerde streetItemUpdateSerde = DejaJsonSerde.<StreetItemUpdateDTO>builder().clazz(StreetItemUpdateDTO.class).build();
        KStream<Long, Integer> productCountChangeStream = streamsBuilder.stream(Constants.KAFKA_STREAM_TOPIC_PRODUCT_PURCHASABLE_COUNT, Consumed.with(Serdes.Long(), Serdes.Integer()));
        productCountChangeStream.transform(new StreetItemPurchasableCountUpdateTransformer()).to(Constants.KAFKA_STREAM_TOPIC_STREET_ITEM_COUNT_CHANGE, Produced.with(Serdes.String(), streetItemUpdateSerde));


        //update street for analysis recommended in real time
  //      productCountChangeStream.transform()



        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("StreetSnapCountChangeMiddleware running ...");
        stream.start();

    }
}
