package codexe.han.kafkadatapipeline.middleware.product;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.common.DejaUtils;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryComboDTO;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryDTO;
import codexe.han.kafkadatapipeline.dto.product.ProductDTO;
import codexe.han.kafkadatapipeline.serde.DejaJsonSerde;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

@Slf4j
@Data
@Builder
public class ProductInventoryValidateMiddleware {

    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");


        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        DejaJsonSerde<ProductInventoryComboDTO> inventoryJsonSerde = DejaJsonSerde.<ProductInventoryComboDTO>builder().clazz(ProductInventoryComboDTO.class).build();

        KStream<String, ProductInventoryComboDTO> inventoryKStream = streamsBuilder.stream(Constants.KAFKA_TOPIC_PRODUCT_INVENTORY, Consumed.with(Serdes.String(), inventoryJsonSerde));

        //get product with purchasable status
        KStream<Long, String> purchasableProductStream = inventoryKStream.map((key, value) -> {
         try{
             return KeyValue.pair(Long.valueOf(key), DejaUtils.validateInventory(value).toString());
         }catch(Exception e){
             log.error("product inventory validate format error {}" ,e );
             return KeyValue.pair(Constants.ERROR_FORMAT_PRODUCT_ID, DejaUtils.validateInventory(value).toString());
         }
        });

        purchasableProductStream.to(Constants.KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE, Produced.with(Serdes.Long(), Serdes.String()));

        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("ProductInventoryValidateMiddleware running ...");
        stream.start();

    }

}
