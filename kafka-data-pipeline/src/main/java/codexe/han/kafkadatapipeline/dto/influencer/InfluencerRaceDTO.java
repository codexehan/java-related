package codexe.han.kafkadatapipeline.dto.influencer;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class InfluencerRaceDTO implements DataBaseDTO {

    @JsonProperty("last_update_time")
    private Long lastUpdateTime;

    @JsonProperty("createTime")
    private Long createTime;

    private String name;

    private Long id;

    @JsonProperty("is_delete")
    private boolean isDelete;

    @Override
    public Object get(String esField) {
       switch(esField){
           case "create_time":
               return this.createTime;
           case "influencer_race_id":
               return this.id;
           case "last_update_time":
               return this.lastUpdateTime;
           case "name":
               return this.name;
           case "is_delete":
               return this.isDelete;
       }
       return null;
    }

    @Override
    public List<String> getEsFieldList() {
        return Arrays.asList(new String[]{"create_time", "influencer_race_id", "last_update_time", "name", "is_delete"});
    }

    @JsonIgnore
    @Override
    public String getEsId() {
        return String.valueOf(this.id);
    }
}
