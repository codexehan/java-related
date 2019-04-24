package codexe.han.kafkadatapipeline.dto.influencer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerStyleRelationDTO {

    @JsonProperty("influencer_style_id")
    private Integer influencerStyleId;

    @JsonProperty("influencer_id")
    private Integer influencerId;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("influecer_style_type")
    private Integer influencerStyleType;

    @JsonProperty("create_time")
    private Long createTime;

    private Long id;

    @JsonProperty("influencer_style_name")
    private String influencerStyleName;

    @JsonProperty("created_date")
    private Long createDate;

    @JsonProperty("last_modified_date")
    private Long lastModifiedDate;
}
