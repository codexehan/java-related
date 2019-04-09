package deja.fashion.datapipeline.dto.eventtracking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import deja.fashion.datapipeline.dto.DataBaseDTO;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventTrackingEvent extends Event {

    private String id;

    @JsonProperty(value="event_body")
    private EventTrackingEventBody eventBody;

}