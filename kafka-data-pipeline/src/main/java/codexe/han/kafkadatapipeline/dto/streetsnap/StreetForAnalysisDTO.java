package codexe.han.kafkadatapipeline.dto.streetsnap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StreetForAnalysisDTO {

    @JsonProperty("like_count")
    private Integer likeCount;

    private String phash;

    @JsonProperty("image_width")
    private Integer imageWidth;

    @JsonProperty("post_time")
    private Long postTime;

    @JsonProperty("eligible_item_count")
    private Integer eligibleItemCount;

    @JsonProperty("image_original_url")
    private String imageOriginalUrl;

    @JsonProperty("is_delete")
    private boolean isDelete;

    private Long reference;

    @JsonProperty("influencer_id")
    private Integer influencerId;

    @JsonProperty("image_height")
    private Integer imageHeight;

    @JsonProperty("image_local_path")
    private String imageLocalPath;

    @JsonProperty("street_type")
    private Integer streetType;

    private Long id;

    @JsonProperty("review_status")
    private Integer reviewStatus;

    private List<Integer> tags;

    @JsonProperty("create_time")
    private Long createTime;


    @JsonProperty("last_update_time")
    private Long lastUpdateTime;
}
