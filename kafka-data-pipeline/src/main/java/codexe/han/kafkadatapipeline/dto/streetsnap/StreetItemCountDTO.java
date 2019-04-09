package deja.fashion.datapipeline.dto.streetsnap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreetItemCountDTO {

    private Long streetForAnalysisId;

    private Integer isPurchasableCount;

}
