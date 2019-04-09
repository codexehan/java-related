package deja.fashion.datapipeline.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("group_id")
    private String groupId;

    @JsonProperty("product_code")
    private String productCode;

    @JsonProperty("merchant_id")
    private Integer merchantId;

    @JsonProperty("brand_id")
    private Integer brandId;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("site_mark")
    private String siteMark;

    @JsonProperty("cloth_id")
    private Integer clothId;

    @JsonProperty("is_ocb")
    private boolean isOcb;

    @JsonProperty("is_delete")
    private boolean isDelete;

    private String name;

    @JsonProperty("brand_url")
    private String brandUrl;

    @JsonProperty("product_color")
    private String productColor;

    private String breadcrumb;

    private String description;

    @JsonProperty("detail_description")
    private String detailDescription;

    @JsonProperty("original_price")
    private Long originalPrice;

    @JsonProperty("current_price")
    private Long currentPrice;

    private String currency;

    @JsonProperty("size_guide_table")
   // private SizeGuideTableDTO sizeGuideTable;
    private String sizeGuideTable;

    @JsonProperty("size_guide_description")
    private String sizeGuideDescription;

    private Integer category;

    @JsonProperty("subcategory")
    private Integer subCategory;

    private Integer color;

    private Integer pattern;

    private Integer length;

    @JsonProperty("sleeve_length")
    private Integer sleeveLength;

    private Integer neckline;

    @JsonProperty("autotag_version")
    private Integer autotagVersion;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("extend_tag")
    private ExtendTagDTO extendTag;

    @JsonProperty("product_image_list")
    private List<ProductImageDTO> productImageList;

    @JsonProperty("other_strs")
    private Map<String, String> otherStrFields;

    @JsonProperty("other_ints")
    private Map<String, Long> otherIntFields;

    @JsonProperty("other_floats")
    private Map<String, Float> otherFloatFields;

}
