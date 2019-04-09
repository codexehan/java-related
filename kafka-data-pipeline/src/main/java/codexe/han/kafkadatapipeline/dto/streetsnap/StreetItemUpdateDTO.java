package deja.fashion.datapipeline.dto.streetsnap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetItemUpdateDTO {
    @JsonProperty("street_item_id_set")
    private Set<Long> streetItemIdSet;

    @JsonProperty("count_change")
    private Integer countChange;
}
