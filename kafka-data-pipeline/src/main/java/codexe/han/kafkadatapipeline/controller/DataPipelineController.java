package deja.fashion.datapipeline.controller;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.consumer.eventtracking.EventTrackingConsumer;
import deja.fashion.datapipeline.consumer.product.ProductConsumer;
import deja.fashion.datapipeline.consumer.product.ProductPriceConsumer;
import deja.fashion.datapipeline.consumer.product.ProductStatusChangeConsumer;
import deja.fashion.datapipeline.consumer.refresh.StreetItemUpdateConsumer;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.eventtracking.EventTrackingEvent;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.dto.product.ProductStatusChangeDTO;
import deja.fashion.datapipeline.middleware.product.ProductFinalStatusValidateMiddleware;
import deja.fashion.datapipeline.middleware.product.ProductPriceValidateMiddleware;
import deja.fashion.datapipeline.middleware.product.ProductStatusChangeMiddleware;
import deja.fashion.datapipeline.middleware.product.ProductValidateMiddleware;
import deja.fashion.datapipeline.properties.DataPipelineProperties;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class DataPipelineController {

   /* private DataPipelineProperties dataPipelineProperties;
    private PipelineKafkaOffsetService pipelineKafkaOffsetService;

    private final String PRODUCT_CONSUMER= "product_consumer";
    private final String PRICE_CONSUMER= "price_consumer";
    private final String PRODUCT_VALIDATE_CONSUMER= "product_validate_consumer";
    private final String PRICE_VALIDATE_CONSUMER = "price_validate_consumer";
    private final String PRODUCT_STATUS_VALIDATE_CONSUMER= "product_status_validate_consumer";
    private final String PRODUCT_STATUS_CHANGE_VALIDATE_CONSUMER= "product_status_change_validate_consumer";
    private final String PRODUCT_STATUS_CHANGE_CONSUMER= "product_status_change_consumer";
    private final String STREET_ITEM_REFRESH_CONSUMER= "street_item_refresh_consumer";
    private final String EVENT_TRACKING_CONSUMER= "event_tracking_consumer";

    private Thread productConsumer_T;
    private Thread productPriceConsumer_T;
    private Thread productFinalStatusValidateMiddleware_T;
    private Thread productStatusChangeMiddleware_T;
    private Thread productStatusChangeConsumer_T;
    private Thread streetItemRefreshConsumer_T;
    private Thread eventTrackingConsumer_T;


    @PutMapping("all_start")
    ResponseEntity allStart(){
        consumeProduct();
        consumeProductPrice();

        productFinalStatusValidateMiddleware();
        productStatusChangeMiddleware();

        consumeProductStatusChange();
        consumeStreetItemRefresh();

        consumeEventTracking();
        return null;
    }



    public void consumeProduct(){
        log.info("---------------Product Consume Thread Start---------------");
        ProductConsumer productConsumer = new ProductConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_TOPIC_PRODUCTS,
                new StringDeserializer(),
                DejaJsonSerde.<ProductDTO>builder().clazz(ProductDTO.class).build(),
                pipelineKafkaOffsetService);
        productConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        this.productConsumer_T = new Thread(() -> productConsumer.consume());
        this.productConsumer_T.setName(PRODUCT_CONSUMER);
        this.productConsumer_T.start();

    }
    public void consumeProductPrice(){
        log.info("---------------Product Price Consume Thread Start---------------");
        ProductPriceConsumer productPriceConsumer = new ProductPriceConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_TOPIC_PRODUCT_PRICE,
                new StringDeserializer(),
                DejaJsonSerde.<ProductPriceDTO>builder().clazz(ProductPriceDTO.class).build(),
                pipelineKafkaOffsetService);
        productPriceConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        productPriceConsumer_T = new Thread(() -> productPriceConsumer.consume());
        productPriceConsumer_T.setName(PRICE_CONSUMER);
        productPriceConsumer_T.start();

    }


    public void productFinalStatusValidateMiddleware(){
        log.info("---------------Product Final Status Validate Middleware Thread Start---------------");
        ProductFinalStatusValidateMiddleware productFinalStatusValidateMiddleware = ProductFinalStatusValidateMiddleware.builder().build();
        productFinalStatusValidateMiddleware_T = new Thread(() -> productFinalStatusValidateMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productFinalStatusValidateMiddleware"));
        productFinalStatusValidateMiddleware_T.setName(PRODUCT_STATUS_VALIDATE_CONSUMER);
        productFinalStatusValidateMiddleware_T.start();
    }
    public void productStatusChangeMiddleware(){
        log.info("---------------Product Status Change Validate Middleware Thread Start---------------");
        ProductStatusChangeMiddleware productStatusChangeMiddleware = ProductStatusChangeMiddleware.builder().build();
        productStatusChangeMiddleware_T = new Thread(() -> productStatusChangeMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productStatusChangeMiddleware"));
        productStatusChangeMiddleware_T.setName(PRODUCT_STATUS_CHANGE_VALIDATE_CONSUMER);
        productStatusChangeMiddleware_T.start();
    }
    public void consumeProductStatusChange(){
        log.info("---------------Product Status Change Consume Thread Start---------------");
        ProductStatusChangeConsumer productStatusChangeConsumer = new ProductStatusChangeConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_TOPIC_PRODUCT_STATUS_CHANGE,
                new LongDeserializer(),
                DejaJsonSerde.<ProductStatusChangeDTO>builder().clazz(ProductStatusChangeDTO.class).build(),
                pipelineKafkaOffsetService);
        productStatusChangeConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        productStatusChangeConsumer_T = new Thread(() -> productStatusChangeConsumer.consume());
        productStatusChangeConsumer_T.setName(PRODUCT_STATUS_CHANGE_CONSUMER);
        productStatusChangeConsumer_T.start();

    }
    public void consumeStreetItemRefresh(){
        log.info("---------------Street Item Update Consume Thread Start---------------");
        StreetItemUpdateConsumer streetItemUpdateConsumer = new StreetItemUpdateConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_TOPIC_STREET_ITEM_REFRESH,
                new StringDeserializer(),
                new StringDeserializer(),
                pipelineKafkaOffsetService);
        streetItemUpdateConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        streetItemRefreshConsumer_T = new Thread(() -> streetItemUpdateConsumer.consume());
        streetItemRefreshConsumer_T.setName(STREET_ITEM_REFRESH_CONSUMER);
        streetItemRefreshConsumer_T.start();

    }

    public void consumeEventTracking(){
        log.info("---------------Event Tracking Consume Thread Start---------------");
        EventTrackingConsumer eventTrackingConsumer = new EventTrackingConsumer(this.dataPipelineProperties.getEventTrackingBootstrapServers(),
                this.dataPipelineProperties.getEventTrackingGroupId(),
                Constants.KAFKA_TOPIC_EVENT_TRACKING,
                new StringDeserializer(),
                DejaJsonSerde.<EventTrackingEvent>builder().clazz(EventTrackingEvent.class).build());
        eventTrackingConsumer.initElasticSearch(this.dataPipelineProperties.getEventTrackingElasticsearchHostname(), this.dataPipelineProperties.getEventTrackingElasticsearchPort(), this.dataPipelineProperties.getEventTrackingElasticsearchScheme(), "","");
        eventTrackingConsumer_T = new Thread(() -> eventTrackingConsumer.consume());
        eventTrackingConsumer_T.setName(EVENT_TRACKING_CONSUMER);
        eventTrackingConsumer_T.start();

    }*/
}
