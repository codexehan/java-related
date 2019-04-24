package codexe.han.kafkadatapipeline.test.productstatus;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.ProductPriceDTO;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryComboDTO;
import codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryDTO;
import codexe.han.kafkadatapipeline.dto.product.*;
import codexe.han.kafkadatapipeline.serde.DejaJsonSerde;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

public class ProductProducer {


    public static void main(String[] args) {
        Long productId = -9999999L;
        DeliColorTop2DTO deliColorTop2DTO = DeliColorTop2DTO.builder()
                .id(45)
                .score(7)
                .build();
        DeliColorTop2DTO deliColorTop2DTO2 = DeliColorTop2DTO.builder()
                .id(2)
                .score(1)
                .build();
        List<DeliColorTop2DTO>  deliColorTop2DTOList = new ArrayList<>();
        deliColorTop2DTOList.add(deliColorTop2DTO);
        deliColorTop2DTOList.add(deliColorTop2DTO2);
        ProductImageDTO imageDTO = ProductImageDTO.builder()
                .height(78)
                .width(45)
                .imageUrl("sdsssssss")
                .attribute(45)
                .phash("sdfasf54235231")
                .build();
        ProductImageDTO imageDTO2 = ProductImageDTO.builder()
                .height(78)
                .width(45)
                .imageUrl("sdsssssss")
                .attribute(45)
                .phash("sdfasf542352314545")
                .build();
        List<ProductImageDTO> productImageDTOList = new ArrayList<>();
        productImageDTOList.add(imageDTO);
        productImageDTOList.add(imageDTO2);
        ProductDTO productDTO = ProductDTO.builder()
                .autotagVersion(1)
                .brandId(2)
                .brandName("xxx")
                .brandUrl("www.shop.com")
                .breadcrumb("")
                .category(3)
                .clothId(1)
                .color(2)
                .createTime(4454242541L)
                .currency("Yuan")
                .currentPrice(245L)
                .description("")
                .detailDescription("sdf")
                .extendTag(ExtendTagDTO.builder()
                        .deliColorTop2(deliColorTop2DTOList)
                        .build())
                .groupId("sdf")
                .isDelete(false)
                .isOcb(true)
                .lastUpdateTime(45645212L)
                .length(45)
                .merchantId(0)
                .name("eqt")
                .neckline(2)
                .originalPrice(450L)
                .pattern(45)
                .productCode("sdfga")
                .productColor("red")
                .productId(productId)
                .productImageList(productImageDTOList)
                .siteMark("site")
                .sizeGuideDescription("desc")
                .sleeveLength(78)
                .subCategory(45)
                .build();

        ProductInventoryDTO productInventoryDTO = ProductInventoryDTO.builder()
                .autoDeleted(true)
                .createTime(354512L)
                .inventoryId(7587421L)
                .lastUpdateTime(784514L)
                .quantity(10)
                .size("s")
                .build();
        ProductInventoryDTO productInventoryDTO2 = ProductInventoryDTO.builder()
                .autoDeleted(false)
                .createTime(354512L)
                .inventoryId(87564155L)
                .lastUpdateTime(784514L)
                .quantity(10)
                .size("s")
                .build();
        List<ProductInventoryDTO> productInventoryDTOList = new ArrayList<>();
        productInventoryDTOList.add(productInventoryDTO);
        productInventoryDTOList.add(productInventoryDTO2);
        ProductInventoryComboDTO productInventoryComboDTO = ProductInventoryComboDTO.builder()
                .productId(productId)
                .productInventoryDTOList(productInventoryDTOList)
                .build();

        ProductPriceDTO productPriceDTO = ProductPriceDTO.builder()
                .productId(productId)
                .currency("new YUan")
                .currentPrice(1L)
                .originalPrice(22L)
                .build();
        //send product
   //     sendProduct(productDTO);
        //send product inventory
       /* for(int i=0;i<1000;i++) {
            sendProductInventory(productInventoryComboDTO);
        }
        productInventoryDTO2.setAutoDeleted(true);
        sendProductInventory(productInventoryComboDTO);*/

       //product_id:5719637 current_price:400 original_price:1,000
       /* ProductPriceDTO productPriceDTO1 = ProductPriceDTO.builder()
                .productId(5674997L)
                .currency("USD")
                .currentPrice(4099L)
                .originalPrice(4099L)
                .build();
            sendPrice(productPriceDTO1);*/
        //send product price
        sendProduct(productDTO);
    }
    public static void sendProduct(ProductDTO productDTO){
      //  String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
      //  String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
        String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        Properties kafkaProps =  new Properties();
        kafkaProps.put("bootstrap.servers", bootstrapServer);

        StringSerializer stringSerializer = new StringSerializer();
        KafkaProducer producer = new KafkaProducer(kafkaProps, stringSerializer, DejaJsonSerde.<ProductDTO>builder().clazz(ProductDTO.class).build());

        List<ProducerRecord> recordList = new ArrayList<>();
        //mock customer behaviors
        recordList.add(new ProducerRecord(Constants.KAFKA_TOPIC_PRODUCTS, productDTO.getProductId().toString(), productDTO));
        for(ProducerRecord record : recordList){
            producer.send(record, (RecordMetadata r, Exception e) -> {
                if(e != null){
                    e.printStackTrace();
                }
            });
        }


        producer.close();
    }
    public static void sendProductInventory(ProductInventoryComboDTO productInventoryDTO){
        String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
        Properties kafkaProps =  new Properties();
        kafkaProps.put("bootstrap.servers", bootstrapServer);

        StringSerializer stringSerializer = new StringSerializer();
        KafkaProducer producer = new KafkaProducer(kafkaProps, stringSerializer, DejaJsonSerde.<ProductInventoryComboDTO>builder().clazz(ProductInventoryComboDTO.class).build());

        List<ProducerRecord> recordList = new ArrayList<>();

        //mock customer behaviors
        recordList.add(new ProducerRecord(Constants.KAFKA_TOPIC_PRODUCT_INVENTORY, productInventoryDTO.getProductId().toString(), productInventoryDTO));
        for(ProducerRecord record : recordList){
            producer.send(record, (RecordMetadata r, Exception e) -> {
                if(e != null){
                    e.printStackTrace();
                }
            });
        }


        producer.close();
    }
    public static void sendPrice(ProductPriceDTO productPriceDTO){
    //    String bootstrapServer = "172.28.10.59:9090,172.28.10.59:9091,172.28.10.59:9092";
        String bootstrapServer = "172.28.2.22:9090,172.28.2.22:9091,172.28.2.22:9092";
        Properties kafkaProps =  new Properties();
        kafkaProps.put("bootstrap.servers", bootstrapServer);

        StringSerializer stringSerializer = new StringSerializer();
        KafkaProducer producer = new KafkaProducer(kafkaProps, stringSerializer, DejaJsonSerde.<ProductPriceDTO>builder().clazz(ProductPriceDTO.class).build());

        List<ProducerRecord> recordList = new ArrayList<>();
        //mock customer behaviors

        recordList.add(new ProducerRecord(Constants.KAFKA_TOPIC_PRODUCT_PRICE, productPriceDTO.getProductId().toString(), productPriceDTO));

        for(ProducerRecord record : recordList){
            producer.send(record, (RecordMetadata r, Exception e) -> {
                if(e != null){
                    e.printStackTrace();
                }
            });
        }


        producer.close();
    }

}
