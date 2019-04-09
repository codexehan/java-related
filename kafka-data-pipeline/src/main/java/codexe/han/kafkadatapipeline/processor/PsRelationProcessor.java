package deja.fashion.datapipeline.processor;

import deja.fashion.datapipeline.common.Constants;
import deja.fashion.datapipeline.dto.streetsnap.PsRelationDTO;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.StateStore;
import org.apache.kafka.streams.state.KeyValueStore;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

public class PsRelationProcessor implements Processor<Long, PsRelationDTO> {

    private ProcessorContext context;

    private KeyValueStore<Long, Set<Long>> productStreetItemRelationStore;

    private KeyValueStore<Long, Long> streetItemStreetSnapRelationStore;

    @Override
    public void init(ProcessorContext context) {
        this.productStreetItemRelationStore = (KeyValueStore<Long, Set<Long>>) context.getStateStore(Constants.LOCAL_STORE_PRODUCT_STREET_ITEM_RELATION);
        this.streetItemStreetSnapRelationStore = (KeyValueStore<Long, Long>) context.getStateStore(Constants.LOCAL_STORE_STREET_ITEM_STREETSNAP_RELATION);
    }

    @Override
    public void process(Long key, PsRelationDTO value) {
        //update product street_item relation
        Set<Long> streetItemIdSet = this.productStreetItemRelationStore.get(value.getProductId());
        if(streetItemIdSet != null){
            streetItemIdSet.add(value.getStreetItemId());
        }
        else{
            streetItemIdSet = new HashSet<>();
            streetItemIdSet.add(value.getStreetItemId());
        }
        this.productStreetItemRelationStore.put(value.getProductId(), streetItemIdSet);

        //update street_item street_for_analysis relation
        this.streetItemStreetSnapRelationStore.put(value.getStreetItemId(), value.getStreetAnalysisId());

    }

    @Override
    public void close() {

    }
}
