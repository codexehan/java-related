package deja.fashion.datapipeline.entrance;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.consumer.eventtracking.EventTrackingConsumer;
import deja.fashion.datapipeline.consumer.product.*;
import deja.fashion.datapipeline.consumer.refresh.StreetItemUpdateConsumer;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.eventtracking.EventTrackingEvent;
import deja.fashion.datapipeline.dto.inventory.ProductInventoryComboDTO;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import deja.fashion.datapipeline.dto.product.ProductStatusChangeDTO;
import deja.fashion.datapipeline.middleware.product.*;
import deja.fashion.datapipeline.properties.DataPipelineProperties;
import deja.fashion.datapipeline.serde.DejaJsonSerde;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class DataPipelineEntrance {
    private DataPipelineProperties dataPipelineProperties;
    private PipelineKafkaOffsetService pipelineKafkaOffsetService;

    private static final String PRODUCT_CONSUMER= "product_consumer";
    private static final String PRICE_CONSUMER= "price_consumer";
    private static final String PRODUCT_VALIDATE_CONSUMER= "product_validate_consumer";
    private static final String PRICE_VALIDATE_CONSUMER = "price_validate_consumer";
    private static final String PRODUCT_STATUS_VALIDATE_CONSUMER= "product_status_validate_consumer";
    private static final String PRODUCT_FINAL_STATUS_CONSUMER= "product_final_status_consumer";
    private static final String PRODUCT_STATUS_CHANGE_VALIDATE_CONSUMER= "product_status_change_validate_consumer";
    private static final String PRODUCT_STATUS_CHANGE_CONSUMER= "product_status_change_consumer";
    private static final String STREET_ITEM_REFRESH_CONSUMER= "street_item_refresh_consumer";
    private static final String EVENT_TRACKING_CONSUMER= "event_tracking_consumer";

    @PostConstruct
    public void start(){
        log.info("data pipeline start");
        consumeProduct();
   //     consumeProductInventory();
        consumeProductPrice();
   //     productValidateMiddleware();
   //     productPriceValidateMiddleware();
   //     productInventoryValidateMiddleware();
        productFinalStatusValidateMiddleware();

        consumeProductFinalStatus();
       /* productStatusChangeMiddleware();
        consumeProductStatusChange();*/
        consumeStreetItemRefresh();

        consumeEventTracking();
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
        Thread thread = new Thread(() -> productConsumer.consume());
        thread.setName(PRODUCT_CONSUMER);
        thread.start();

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
        Thread thread = new Thread(() -> productPriceConsumer.consume());
        thread.setName(PRICE_CONSUMER);
        thread.start();

    }
    /*public void consumeProductInventory(){
        log.info("---------------Product Inventory Consume Thread Start---------------");
        ProductInventoryConsumer productInventoryConsumer = new ProductInventoryConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_TOPIC_PRODUCT_INVENTORY,
                new StringDeserializer(),
                DejaJsonSerde.<ProductInventoryComboDTO>builder().clazz(ProductInventoryComboDTO.class).build(),
                pipelineKafkaOffsetService);
        productInventoryConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        new Thread(() -> productInventoryConsumer.consume()).start();

    }*/

    public void productValidateMiddleware(){
        log.info("---------------Product Validate Middleware Thread Start---------------");
        ProductValidateMiddleware productValidateMiddleware = ProductValidateMiddleware.builder().build();
        Thread thread = new Thread(() -> productValidateMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productValidateMiddleware"));
        thread.setName(PRODUCT_VALIDATE_CONSUMER);
        thread.start();

    }
    public void productPriceValidateMiddleware(){
        log.info("---------------Product Price Validate Middleware Thread Start---------------");
        ProductPriceValidateMiddleware productPriceValidateMiddleware = ProductPriceValidateMiddleware.builder().build();
        Thread thread = new Thread(() -> productPriceValidateMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productPriceValidateMiddleware"));
        thread.setName(PRICE_VALIDATE_CONSUMER);
        thread.start();

    }
   /* public void productInventoryValidateMiddleware(){
        log.info("---------------Product Inventory Validate Middleware Thread Start---------------");
        ProductInventoryValidateMiddleware productinventoryValidateMiddleware = ProductInventoryValidateMiddleware.builder().build();
        new Thread(() -> productinventoryValidateMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productInventoryValidateMiddleware")).start();

    }*/
    public void productFinalStatusValidateMiddleware(){
        log.info("---------------Product Final Status Validate Middleware Thread Start---------------");
        ProductFinalStatusValidateMiddleware productFinalStatusValidateMiddleware = ProductFinalStatusValidateMiddleware.builder().build();
        Thread thread = new Thread(() -> productFinalStatusValidateMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productFinalStatusValidateMiddleware"));
        thread.setName(PRODUCT_STATUS_VALIDATE_CONSUMER);
        thread.start();
    }
    public void productStatusChangeMiddleware(){
        log.info("---------------Product Status Change Validate Middleware Thread Start---------------");
        ProductStatusChangeMiddleware productStatusChangeMiddleware = ProductStatusChangeMiddleware.builder().build();
        Thread thread = new Thread(() -> productStatusChangeMiddleware.process(this.dataPipelineProperties.getBootstrapServers(),"productStatusChangeMiddleware"));
        thread.setName(PRODUCT_STATUS_CHANGE_VALIDATE_CONSUMER);
        thread.start();
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
        Thread thread = new Thread(() -> productStatusChangeConsumer.consume());
        thread.setName(PRODUCT_STATUS_CHANGE_CONSUMER);
        thread.start();

    }
    public void consumeProductFinalStatus(){
        log.info("---------------Product Final Status Consume Thread Start---------------");
        ProductFinalStatusConsumer productFinalStatusConsumer = new ProductFinalStatusConsumer(this.dataPipelineProperties.getBootstrapServers(),
                this.dataPipelineProperties.getGroupId(),
                Constants.KAFKA_STREAM_TOPIC_PRODUCT_FINAL_STATUS,
                new LongDeserializer(),
                new StringDeserializer(),
                pipelineKafkaOffsetService);
        productFinalStatusConsumer.initElasticSearch(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme(), "","");
        Thread thread = new Thread(() -> productFinalStatusConsumer.consume());
        thread.setName(PRODUCT_FINAL_STATUS_CONSUMER);
        thread.start();

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
        Thread thread = new Thread(() -> streetItemUpdateConsumer.consume());
        thread.setName(STREET_ITEM_REFRESH_CONSUMER);
        thread.start();

    }

    public void consumeEventTracking(){
        log.info("---------------Event Tracking Consume Thread Start---------------");
        EventTrackingConsumer eventTrackingConsumer = new EventTrackingConsumer(this.dataPipelineProperties.getEventTrackingBootstrapServers(),
                this.dataPipelineProperties.getEventTrackingGroupId(),
                Constants.KAFKA_TOPIC_EVENT_TRACKING,
                new StringDeserializer(),
                DejaJsonSerde.<EventTrackingEvent>builder().clazz(EventTrackingEvent.class).build());
        eventTrackingConsumer.initElasticSearch(this.dataPipelineProperties.getEventTrackingElasticsearchHostname(), this.dataPipelineProperties.getEventTrackingElasticsearchPort(), this.dataPipelineProperties.getEventTrackingElasticsearchScheme(), "","");
        Thread thread = new Thread(() -> eventTrackingConsumer.consume());
        thread.setName(EVENT_TRACKING_CONSUMER);
        thread.start();

    }
}
