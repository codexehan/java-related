package deja.fashion.datapipeline.dto.inventory;

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
public class ProductInventoryDTO {

    @JsonProperty("inventory_id")
    private Long inventoryId;

    private Integer quantity;

    private String size;

    @JsonProperty("auto_deleted")
    private boolean autoDeleted;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("create_time")
    private Long createTime;
}
