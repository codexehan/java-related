package deja.fashion.datapipeline.dto.influencer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfluencerDTO {

    @JsonProperty("account_type")
    private Integer accountType;

    @JsonProperty("reference_id")
    private Long referenceId;

    private Integer race;

    @JsonProperty("display_name")
    private String displayName;

    private String platform;

    @JsonProperty("is_delete")
    private boolean isDelete;

    @JsonProperty("avatar_local_path")
    private String avatarLocalPath;

    @JsonProperty("original_name")
    private String originalName;

    @JsonProperty("avatar_original_url")
    private String avatarOriginalUrl;

    private Long id;

    private Integer region;

    @JsonProperty("vip_status")
    private Integer vipStatus;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;
}
