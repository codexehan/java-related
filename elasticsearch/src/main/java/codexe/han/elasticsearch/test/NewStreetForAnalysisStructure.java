package codexe.han.elasticsearch.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

@Slf4j
public class NewStreetForAnalysisStructure {

    /**
     *
     * 添加自定义分词器
     PUT /deja_street_for_analysis/_settings
     {
     "analysis": {
     "filter": {
     "english_stop": {
     "type":       "stop",
     "stopwords":  "_english_"
     },
     "light_english_stemmer": {
     "type":       "stemmer",
     "language":   "light_english"
     },
     "english_possessive_stemmer": {
     "type":       "stemmer",
     "language":   "possessive_english"
     },
     "my_shingle_filter": {
     "type":             "shingle",
     "min_shingle_size": 2,
     "max_shingle_size": 2,
     "output_unigrams":  false
     },
     "graph_synonyms" : {
     "type" : "synonym_graph",
     "synonyms_path" : "synonyms/synonyms.txt"
     }
     },
     "analyzer": {
     "light_english_analyzer": {
     "tokenizer":  "standard",
     "filter": [
     "english_possessive_stemmer",
     "lowercase",
     "english_stop",
     "light_english_stemmer",
     "asciifolding"
     ]
     },
     "shingle_analyzer": {
     "type":             "custom",
     "tokenizer":        "standard",
     "filter": [
     "lowercase",
     "my_shingle_filter"
     ]
     },
     "keyword_analyzer": {
     "tokenizer":  "keyword",
     "filter": [
     "lowercase",
     "asciifolding"
     ]
     },
     "search_synonyms" : {
     "tokenizer" : "whitespace",
     "filter" : ["lowercase","graph_synonyms"]
     }
     }
     }
     }
     *
     */



    public static void main(String[] args) {
        String ip = "172.28.10.67";

        int port = 9200;
        String schema = "http";

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(ip, port, schema)));

        updateMapping(client, "deja_street_for_analysis", buildGarmentName(false));
    }

    public static boolean updateMapping(RestHighLevelClient client, String index, XContentBuilder builder){
        try{
            PutMappingRequest request = new PutMappingRequest(index);
            request.type("tags");
            request.source(builder);
            AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
            log.info("update street for analysis field : {}", response.isAcknowledged());
            return response.isAcknowledged();
        }catch(Exception e){
            log.error("update street for analysis filed error", e);
        }
        return false;
    }

    public static XContentBuilder buildGarmentName(boolean isCreate){
        try{
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                if(isCreate) {
                    builder.startObject("mapping");
                    {
                        builder.startObject("tags");
                        {
                            builder.startObject("properties");
                            {
                                configAnalyzer("garment1_name", builder, false);
                                configAnalyzer("garment2_name", builder, false);
                                configAnalyzer("garment3_name", builder, false);
                                configAnalyzer("garment4_name", builder, false);
                                configAnalyzer("garment5_name", builder, false);
                            }
                            builder.endObject();
                        }
                        builder.endObject();
                    }
                    builder.endObject();
                }
                else{
                   builder.startObject("properties");
                    {
                        configAnalyzer("garment1_name", builder, false);
                        configAnalyzer("garment2_name", builder, false);
                        configAnalyzer("garment3_name", builder, false);
                        configAnalyzer("garment4_name", builder, false);
                        configAnalyzer("garment5_name", builder, false);
                    }
                   builder.endObject();
                }
            }
            builder.endObject();
            return builder;
        } catch (IOException e) {
            log.error("build garment name error", e);
        }
        return null;
    }

    public static void configAnalyzer(String fieldName, XContentBuilder builder, boolean hasKeyword){
        try {
            builder.startObject(fieldName);
            {
                builder.field("type", "text");
                builder.field("analyzer", "light_english_analyzer");

                builder.startObject("fields");
                {
                    builder.startObject("std");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "standard");
                    }
                    builder.endObject();
                    builder.startObject("sig");
                    {
                        builder.field("type", "text");
                        builder.field("analyzer", "shingle_analyzer");
                    }
                    builder.endObject();
                    if(hasKeyword){
                        builder.startObject("keyword");
                        {
                            builder.field("type", "text");
                            builder.field("analyzer", "keyword_analyzer");
                        }
                        builder.endObject();
                    }
                }
                builder.endObject();
            }
            builder.endObject();
        }catch(Exception e){
            log.error("config analyzer error",e);
        }
    }
}
