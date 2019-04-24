package codexe.han.kafkadatapipeline.dto.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductInventoryComboDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("inventory_list")
    private List<codexe.han.kafkadatapipeline.dto.inventory.ProductInventoryDTO> productInventoryDTOList;
}
