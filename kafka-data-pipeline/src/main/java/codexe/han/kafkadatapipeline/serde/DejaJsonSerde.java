package deja.fashion.datapipeline.serde;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import deja.fashion.datapipeline.dto.product.ProductDTO;
import lombok.Builder;
import lombok.Data;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.util.Map;

@Builder
@Data
public class DejaJsonSerde<T> implements Serializer<T>, Deserializer<T>, Serde<T> {

    private Class<T> clazz;


    public static ObjectMapper getObjectMapper(){
        JsonFactory jfactory = new JsonFactory();
        jfactory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jfactory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        ObjectMapper objectMapper = new ObjectMapper(jfactory);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }


    @Override
    public void configure(final Map<String, ?> configs, final boolean isKey) {}

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(final String topic, final byte[] data) {
        if (data == null) {
            return null;
        }

        /*try {
            return getObjectMapper().readValue(data, clazz);
        } catch (final IOException e) {
            throw new SerializationException(e);
        }*/
        try {
            return getObjectMapper().readValue(data, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public byte[] serialize(final String topic, final T data) {
        if (data == null) {
            return null;
        }

        try {
            return getObjectMapper().writeValueAsBytes(data);
        } catch (final Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public void close() {}

    @Override
    public Serializer<T> serializer() {
        return this;
    }

    @Override
    public Deserializer<T> deserializer() {
        return this;
    }

}
