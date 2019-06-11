package codexe.han.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * This is a slack attachments part
 */
public class SlackMessageDTO {
    @JsonProperty("pretext")
    private String pretext;

    @JsonProperty("url")
    private String messageUrl;

    @Builder.Default
    @JsonProperty("ts")
    private long time = Instant.now().getEpochSecond();

    @Builder.Default
    @JsonProperty("color")
    private String color = "good";

    @JsonProperty("fields")
    private List<SlackDetailMessageDTO> slackDetailList;

    @JsonProperty("actions")
    private List<SlackActionDTO> slackActionList;

    public void addSlackDetail(SlackDetailMessageDTO dto){
        if(slackDetailList == null){
            slackDetailList = new ArrayList<>();
        }
        slackDetailList.add(dto);
    }

    public void addSlackAction(SlackActionDTO dto){
        if(slackActionList == null){
            slackActionList = new ArrayList<>();
        }
        slackActionList.add(dto);
    }
}
