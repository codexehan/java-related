package codexe.han.slack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlackDetailMessageDTO {

    private String title;

    private String value;

    @JsonProperty("short")
    @Builder.Default
    private boolean Short=true;

}
