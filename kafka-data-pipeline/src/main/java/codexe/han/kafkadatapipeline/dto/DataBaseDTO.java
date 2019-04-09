package deja.fashion.datapipeline.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;


public interface DataBaseDTO {


    @JsonIgnore
    Object get(String esField);
    

    @JsonIgnore
    List<String> getEsFieldList();

    @JsonIgnore
    String getEsId();

}
