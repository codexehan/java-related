package deja.fashion.datapipeline.processor;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.dto.product.ProductStatusChangeDTO;
import org.apache.kafka.streams.kstream.ValueTransformerWithKey;
import org.apache.kafka.streams.kstream.ValueTransformerWithKeySupplier;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

/**
 * Generate count change regrading to purchasable status of product used to update is_purchasable_count field of street_item
 */
public class ProductPurchasableChangeTransformer implements ValueTransformerWithKeySupplier<Long, String, ProductStatusChangeDTO> {

    @Override
    public ValueTransformerWithKey<Long, String, ProductStatusChangeDTO> get() {
        return new ValueTransformerWithKey<Long, String, ProductStatusChangeDTO>() {
            private KeyValueStore<Long, String> state;

            public void init(ProcessorContext context) {
                this.state = (KeyValueStore<Long, String>)context.getStateStore(Constants.LOCAL_STORE_PRODUCT_PURCHASABLE_STATUS_CHANGE);
            }

            @Override
            public ProductStatusChangeDTO transform(Long productId, String value) {
                String oldPurchasableValue = this.state.get(productId);
                Boolean newPurchasableValue = Boolean.valueOf(value);

                if(oldPurchasableValue == null){
                    //save to the local store
                    this.state.put(productId, value);
                    return ProductStatusChangeDTO.builder().isPurchasable(newPurchasableValue).needUpdate(true).build();
                }
                else{
                    if(Boolean.valueOf(oldPurchasableValue).compareTo(newPurchasableValue)==0){//both are true or false
                        return ProductStatusChangeDTO.builder().isPurchasable(newPurchasableValue).needUpdate(false).build();
                    }
                    else{
                        //save to the local store
                        this.state.put(productId, value);
                        return ProductStatusChangeDTO.builder().isPurchasable(newPurchasableValue).needUpdate(true).build();
                    }
                }
            }
            public void close() {
                // can access this.state
            }
        };
    }
}
