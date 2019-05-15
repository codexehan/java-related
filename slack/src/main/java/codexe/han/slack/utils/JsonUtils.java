package codexe.han.slack.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

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
            ex.printStackTrace();
            return "object mapper mapping object ot json failed";
        }
    }
}
