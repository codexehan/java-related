package codexe.han.kafkadatapipeline.dto;

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
public class ProductPriceDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("original_price")
    private Long originalPrice;

    @JsonProperty("current_price")
    private Long currentPrice;

    private String currency;

    @JsonProperty("is_offline")
    private Boolean isOffline;
}
