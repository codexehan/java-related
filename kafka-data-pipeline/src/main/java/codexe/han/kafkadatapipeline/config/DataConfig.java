package deja.fashion.datapipeline.config;

import deja.fashion.datapipeline.client.ElasticsearchClient;
import deja.fashion.datapipeline.properties.DataPipelineProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig {
    private DataPipelineProperties dataPipelineProperties;
   /* @Bean
    public ElasticsearchClient elasticsearchClient(){
        return ElasticsearchClient.build(this.dataPipelineProperties.getElasticsearchHostname(), this.dataPipelineProperties.getElasticsearchPort(), this.dataPipelineProperties.getElasticsearchScheme());
    }*/
}
