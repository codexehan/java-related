package codexe.han.kafkadatapipeline.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStatusChangeDTO {

    @JsonProperty("is_purchasable")
    private boolean isPurchasable;

    @JsonProperty("need_update")
    private boolean needUpdate;
}
