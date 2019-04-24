package codexe.han.kafkadatapipeline.processor;

import codexe.han.kafkadatapipeline.common.Constants;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.kstream.ValueTransformerWithKeySupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * Generate count change regrading to purchasable status of product used to update is_purchasable_count field of street_item
 */
public class ProductPurchasableTransformer implements ValueTransformerWithKeySupplier<Long, String, Integer> {

    @Override
    public ValueTransformerWithKey<Long, String, Integer> get() {
        return new ValueTransformerWithKey<Long, String, Integer>() {
            private KeyValueStore<Long, String> state;

            public void init(ProcessorContext context) {
                this.state = (KeyValueStore<Long, String>)context.getStateStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS);
            }

            @Override
            public Integer transform(Long productId, String value) {
                String oldPurchasableValue = this.state.get(productId);
                Boolean newPurchasableValue = Boolean.valueOf(value);
                //save to the local store
                this.state.put(productId, value);

                if(oldPurchasableValue == null){
                    return Boolean.valueOf(newPurchasableValue) ?1:0;//initial value is false, no need to update count field
                }
                else{
                    if(Boolean.valueOf(oldPurchasableValue)){
                        return newPurchasableValue?0:-1;//(true->true)->0; (true->false)->-1
                    }
                    else{
                        return newPurchasableValue?1:0;//(false->true)->1; (false->false)->0
                    }
                }
            }
            public void close() {
                // can access this.state
            }
        };
    }
}
