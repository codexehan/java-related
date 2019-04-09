package deja.fashion.datapipeline.middleware.product;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.common.DejaUtils;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Properties;

@Slf4j
@Data
@Builder
public class ProductFinalStatusValidateMiddleware {

    public void process(String bootstrapServer, String applicationId){
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, "org.apache.kafka.streams.errors.LogAndContinueExceptionHandler");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");


        //build topology
        StreamsBuilder streamsBuilder = new StreamsBuilder();


        KTable<Long, String> productValidateTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_VALIDATE, Consumed.with(Serdes.Long(), Serdes.String()));
        KTable<Long, String> priceValidateTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_PRICE_VALIDATE, Consumed.with(Serdes.Long(), Serdes.String()));
        //3.1.2 remove
 //       KTable<Long, String> inventoryValidateTable = streamsBuilder.table(Constants.KAFKA_STREAM_TOPIC_PRODUCT_INVENTORY_VALIDATE, Consumed.with(Serdes.Long(), Serdes.String()));

        KTable<Long, Boolean> finalStatusTable = productValidateTable.leftJoin(priceValidateTable, (leftValue, rightValue)->{
            if(rightValue == null){//no price info
                return false;
            }
            else{
                return Boolean.valueOf(leftValue) && Boolean.valueOf(rightValue);
            }
        });
        //3.1.2 remove
        /*.leftJoin(inventoryValidateTable,(leftValue, rightValue)->{
            if(rightValue == null){//no inventory info
              //  return false;//2.7
                return leftValue;//no need to check inventory  2.8
            }
            else {
                return Boolean.valueOf(leftValue) && Boolean.valueOf(rightValue);//if there are mistakes from data team to send inventory info
            }
        });*/


        finalStatusTable.toStream().mapValues(value -> value.toString()).to(Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS, Produced.with(Serdes.Long(), Serdes.String()));

        KafkaStreams stream = new KafkaStreams(streamsBuilder.build(), props);

        log.info("ProductValidateMiddleware running ...");
        stream.start();



    }


}
