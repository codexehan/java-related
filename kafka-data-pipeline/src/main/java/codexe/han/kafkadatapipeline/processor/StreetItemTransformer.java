package codexe.han.kafkadatapipeline.processor;

import codexe.han.kafkadatapipeline.common.Constants;
import codexe.han.kafkadatapipeline.dto.streetsnap.StreetItemDTO;
import codexe.han.kafkadatapipeline.dto.streetsnap.StreetItemUpdateDTO;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StreetItemTransformer{}/* implements TransformerSupplier<Long, StreetItemDTO, KeyValueStore<Long, String>> {


   *//* @Override
    public Transformer<Long, StreetItemDTO, KeyValueStore<Long, String>> get() {
        return new Transformer<Long, StreetItemDTO, KeyValue<Long, String>>() {
            private KeyValueStore<Long, Integer> streetItemCountStore;

            private KeyValueStore<Long, Set<Long>> streetForAnalysisRecommendedStore;

            public void init(ProcessorContext context) {
                this.streetItemCountStore = (KeyValueStore<Long, Integer>) context.getStateStore(Constants.LOCAL_STORE_STREET_ITEM_PURCHASABLE_COUNT_STATUS);

                this.streetForAnalysisRecommendedStore = (KeyValueStore<Long, Set<Long>>) context.getStateStore(Constants.LOCAL_STORE_STREET_SNAP_RECOMMENDED_STATUS);
            }


            @Override
            public KeyValue<Long, String> transform(Long key, StreetItemDTO value) {
                this.streetItemCountStore.put(value.getId(), value.getIsPurchasableCount());

                //update street for analysis recommended status
                if(value.getIsPurchasableCount()>=3){
                    Set<Long> streetItemSet = this.streetForAnalysisRecommendedStore.get(value.getStreetForAnalysisId());
                    if(streetItemSet != null){
                        streetItemSet.add(value.getId());
                    }
                    else{
                        streetItemSet = new HashSet<>();
                        streetItemSet.add(value.getId());
                    }
                    this.streetForAnalysisRecommendedStore.put(value.getStreetForAnalysisId(), streetItemSet);
                    return KeyValue.pair(value.getStreetForAnalysisId(), "true");
                }
                return KeyValue.pair(value.getStreetForAnalysisId(), "true");
            }

            public void close() {
                // can access this.state
            }
        };
    }*//*
   return null;
}*/
