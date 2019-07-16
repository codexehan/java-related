package codexe.han.kafkadatapipeline.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageDTO {

    private String phash;

    @JsonProperty("image_url")
    private String imageUrl;

    private Integer width;

    private Integer height;

    private Integer attribute;
}
