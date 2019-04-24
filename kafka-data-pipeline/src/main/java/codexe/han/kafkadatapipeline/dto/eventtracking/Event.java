package codexe.han.kafkadatapipeline.dto.eventtracking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event{

    @JsonProperty(value="event_name")
    private String eventName;

    @JsonProperty(value="event_version")
    private String eventVersion;

    @JsonProperty(value="event_ts")
    private Long eventTime;
}
