package deja.fashion.datapipeline.processor;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.dto.streetsnap.StreetItemUpdateDTO;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Set;
import java.util.UUID;

public class StreetForAnalysisRecommendedTransformer {}/* implements TransformerSupplier<Long, Integer, KeyValue<String, StreetItemUpdateDTO>> {
    @Override
    public Transformer<Long, Integer, KeyValue<String, StreetItemUpdateDTO>> get() {
        return new Transformer<Long, Integer, KeyValue<Long, StreetItemUpdateDTO>>() {
            private KeyValueStore<Long, Set<Long>> pruductStreetItemRelationSerde;
          *//*  private KeyValueStore<Long, Set<Long>> pruductStreetItemRelationSerde;
            private KeyValueStore<Long, Set<Long>> pruductStreetItemRelationSerde;*//*

            public void init(ProcessorContext context) {
                this.pruductStreetItemRelationSerde = (KeyValueStore<Long, Set<Long>>)context.getStateStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS);
            }

            @Override
            public KeyValue<String, StreetItemUpdateDTO> transform(Long key, Integer value) {
                Set<Long> streetItemIdSet = this.pruductStreetItemRelationSerde.get(key);
                StreetItemUpdateDTO updateDTO = StreetItemUpdateDTO.builder().streetItemIdSet(streetItemIdSet).countChange(value).build();
                return KeyValue.pair(UUID.randomUUID().toString(), updateDTO);
            }

            public void close() {
                // can access this.state
            }
        };
    }
}*/
