package deja.fashion.datapipeline.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import deja.fashion.datapipeline.dto.ProductPriceDTO;
import deja.fashion.datapipeline.dto.inventory.ProductInventoryComboDTO;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
public class DejaUtils {
    public static Integer calculateDiscountPercentage(Long currentPrice,Long originalPrice){
        try{
            return new BigDecimal(currentPrice).divide(new BigDecimal(originalPrice),10,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1000000)).intValue();
        } catch (Exception e){
            return 1000000;//default discount_percentage
        }
    }

    public static boolean isNewArrival(Long createTime){
        return (new Date().getTime()/1000 - createTime) <= 1209600;//14*24*60*60
    }

    public static Integer getColorAndPattern(Integer color, Integer pattern){

        if(color==null && pattern==null)
            return 0;
        else{
            if(color==null && pattern!=null){
                return pattern;
            }
            else if(color!=null && pattern==null){
                return color;
            }
        }
        return pattern>0?pattern:color;
    }

    public static Boolean validateProduct(ProductDTO productDTO){
        try{
           return !StringUtils.isEmpty(productDTO.getGroupId())
                   &&!StringUtils.isEmpty(productDTO.getProductCode())
                   &&!StringUtils.isEmpty(productDTO.getName())
                   &&(productDTO.getBrandId()!=null && productDTO.getBrandId() != 0)
                   &&!StringUtils.isEmpty(productDTO.getBrandName())
                   &&!StringUtils.isEmpty(productDTO.getProductColor())
                   &&(productDTO.getCategory()!=null&&productDTO.getCategory()!=0)
                  /* &&!StringUtils.isEmpty(productDTO.getCurrency())
                   &&!(productDTO.getCurrentPrice()<0)
                   &&!(productDTO.getCurrentPrice() > productDTO.getOriginalPrice())*/ //price will be validate individually
                   &&!(StringUtils.isEmpty(productDTO.getProductImageList().get(0).getImageUrl()))
                   &&productDTO.isOcb()
                   &&!productDTO.isDelete();
        }catch(Exception e){
            log.error("validate product error", e);
            return false;
        }
    }

    public static Boolean validatePrice(ProductPriceDTO productPriceDTO){
        try{
            return  !StringUtils.isEmpty(productPriceDTO.getCurrency())
                    &&!(productPriceDTO.getCurrentPrice()<=0)
                    &&!(productPriceDTO.getCurrentPrice() > productPriceDTO.getOriginalPrice())
                    &&(productPriceDTO.getIsOffline()==null || !productPriceDTO.getIsOffline());//need combine different types of data after adding isOffline field
        }catch(Exception e){
            log.error("validate product price error", e);
            return false;
        }
    }

    public static Boolean validateInventory(ProductInventoryComboDTO productInventoryComboDTO){
        try{
            return productInventoryComboDTO.getProductInventoryDTOList().stream().anyMatch(inventory -> !inventory.isAutoDeleted()&&inventory.getQuantity()>0);
        }catch(Exception e){
            log.error("validate product price error", e);
            return false;
        }
    }

    public static void fromBegining(KafkaConsumer consumer){
        //consumer from beginning
        // So call poll()
        consumer.poll(0);
        // Now there is heartbeat and consumer is "alive"
        consumer.seekToBeginning(consumer.assignment());
    }

    public static long getPriceToSGDPrice(String currency,long price)  {
        if(currency.equals("SGD") || currency.equals("S$")){
            return price;
        }else if(currency.equals("USD")){
            return new BigDecimal(price).multiply(new BigDecimal(1.50)).longValue();
        }else if(currency.equals("US$")){
            return new BigDecimal(price).multiply(new BigDecimal(1.50)).longValue();
        }else if(currency.equals("AUD") || currency.equals("$")){
            return new BigDecimal(price).multiply(new BigDecimal(1.20)).longValue();
        }else if(currency.equals("EUR")){
            return new BigDecimal(price).multiply(new BigDecimal(1.59)).longValue();
        }else if(currency.equals("GBP")){
            return new BigDecimal(price).multiply(new BigDecimal(1.85)).longValue();
        }else if(currency.equals("MYR")){
            return new BigDecimal(price).multiply(new BigDecimal(0.35)).longValue();
        }else if(currency.equals("NO_PRICE")){
            return 0L;
        }else if(currency.equals("")){
            return price;
        }else{
            return  0L;
        }
    }

    public static String filterGroupId(String groupId){
        return groupId.replace("/","_");
    }


    //even tracking use
    public static ObjectMapper getObjectMapper(){
        JsonFactory jfactory = new JsonFactory();
        jfactory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jfactory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        ObjectMapper objectMapper = new ObjectMapper(jfactory);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    public static String toJson(Object object){
        try{
            return getObjectMapper().writeValueAsString(object);
        }catch (JsonProcessingException ex){
            return "object mapper mapping object ot json failed";
        }
    }

}
