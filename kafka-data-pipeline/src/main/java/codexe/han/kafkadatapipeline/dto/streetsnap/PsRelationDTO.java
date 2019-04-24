package codexe.han.kafkadatapipeline.dto.streetsnap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PsRelationDTO {

    @JsonProperty("street_analysis_id")
    private Long streetAnalysisId;

    private Float distance;

    @JsonProperty("sub_category")
    private Integer subCategory;

    @JsonProperty("street_item_id")
    private Long streetItemId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("review_status")
    private Integer reviewStatus;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("is_purchasable")
    private boolean isPurchasable;
}
