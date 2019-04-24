package codexe.han.kafkadatapipeline.processor;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.streetsnap.StreetItemUpdateDTO;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Set;
import java.util.UUID;

/**
 * Generate count change regrading to purchasable status of product used to update is_purchasable_count field of street_item
 */
public class StreetItemPurchasableCountUpdateTransformer implements TransformerSupplier<Long, Integer, KeyValue<String, StreetItemUpdateDTO>> {

    @Override
    public Transformer<Long, Integer, KeyValue<String, StreetItemUpdateDTO>> get() {
        return new Transformer<Long, Integer, KeyValue<String, StreetItemUpdateDTO>>() {
            private KeyValueStore<Long, Set<Long>> pruductStreetItemRelationSerde;

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
}
