package deja.fashion.datapipeline.dto.eventtracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventTrackingEventBody {

    @JsonProperty(value = "uid")
    private Long uid;

    @JsonProperty(value = "optional")
    private Object optional;

}