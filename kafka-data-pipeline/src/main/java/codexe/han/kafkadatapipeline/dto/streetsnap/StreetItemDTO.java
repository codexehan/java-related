package codexe.han.kafkadatapipeline.dto.streetsnap;

import com.fasterxml.jackson.annotation.JsonProperty;
import codexe.han.kafkadatapipeline.dto.DataBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetItemDTO implements DataBaseDTO {

    @JsonProperty("sub_category")
    private Integer subCategory;

    @JsonProperty("magic_point_hsv")
    private String magicPointHsv;

    @JsonProperty("magic_point_position")
    private String magicPointPosition;

    @JsonProperty("street_for_analysis_id")
    private Long streetForAnalysisId;

    @JsonProperty("parsed_path")
    private String parsedPath;

    private Long id;

    @JsonProperty("review_status")
    private Integer reviewStatus;

    @JsonProperty("is_eligible")
    private boolean isEligible;

    @JsonProperty("parsed_area")
    private Integer parsedArea;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("is_purchasable_count")
    private Integer isPurchasableCount;

    @Override
    public Object get(String esField) {
        if(esField.equals("create_time")){
            return this.createTime;
        }
        else if(esField.equals("is_eligible")){
            return this.isEligible;
        }
        else if(esField.equals("is_purchasable_count")){
            return this.isPurchasableCount;
        }
        else if(esField.equals("last_update_time")){
            return this.lastUpdateTime;
        }
        else if(esField.equals("magic_point_hsv")){
            return this.magicPointHsv;
        }
        else if(esField.equals("magic_point_position")){
            return this.magicPointPosition;
        }
        else if(esField.equals("parsed_area")){
            return this.parsedArea;
        }
        else if(esField.equals("parsed_path")){
            return this.parsedPath;
        }
        else if(esField.equals("review_status")){
            return this.reviewStatus;
        }
        else if(esField.equals("street_for_analysis_id")){
            return this.streetForAnalysisId;
        }
        else if(esField.equals("street_item_id")){
            return this.id;
        }
        else if(esField.equals("sub_category")){
            return this.subCategory;
        }
        return null;
    }

    @Override
    public List<String> getEsFieldList() {
        return Arrays.asList(new String[]{"create_time", "is_eligible", "is_purchasable_count", "last_update_time", "magic_point_hsv", "magic_point_position", "parsed_area","parsed_path","review_status","street_for_analysis_id","street_item_id","sub_category"});
    }

    @Override
    public String getEsId() {
        return String.valueOf(id);
    }
}
