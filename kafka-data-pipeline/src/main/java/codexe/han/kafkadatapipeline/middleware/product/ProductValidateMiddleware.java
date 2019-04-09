package deja.fashion.datapipeline.middleware.product;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.common.DejaUtils;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Data
@Builder
public class ProductValidateMiddleware {

    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");

        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        DejaJsonSerde<ProductDTO> productJsonSerde = DejaJsonSerde.<ProductDTO>builder().clazz(ProductDTO.class).build();

        KStream<String, ProductDTO> productKStream = streamsBuilder.stream(Constants.KAFKA_TOPIC_PRODUCTS, Consumed.with(Serdes.String(), productJsonSerde));

        //get product with purchasable status
        KStream<Long, String> productDetailValidateStream = productKStream.map((key, value) -> {
            try {
                return KeyValue.pair(Long.valueOf(key), DejaUtils.validateProduct(value).toString());
            }catch(Exception e){
                log.error("product validate format error {}", e);
                return KeyValue.pair(Constants.ERROR_FORMAT_PRODUCT_ID, DejaUtils.validateProduct(value).toString());
            }
        });

        productDetailValidateStream.to(Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE, Produced.with(Serdes.Long(), Serdes.String()));

        //price need to be validated individually
        KStream<Long, String> productPriceValidateStream = productKStream.map((key, value) -> {
         try{
             return KeyValue.pair(Long.valueOf(key), DejaUtils.validatePrice(ProductPriceDTO.builder()
                     .productId(value.getProductId())
                     .currency(value.getCurrency())
                     .currentPrice(value.getCurrentPrice())
                     .originalPrice(value.getOriginalPrice())
                     .isOffline(false).build()).toString());
         }catch(Exception e){
            log.error("product validate format error {}", e);
             return KeyValue.pair(Constants.ERROR_FORMAT_PRODUCT_ID, "false");
         }
        });

        productPriceValidateStream.to(Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE, Produced.with(Serdes.Long(), Serdes.String()));



        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("ProductValidateMiddleware running ...");
        stream.start();


    }

}
