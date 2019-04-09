package deja.fashion.datapipeline.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "data-pipeline-properties")
public class DataPipelineProperties {

    private String bootstrapServers;

    private String groupId;

    private int consumerCount;

    private String elasticsearchHostname;

    private int elasticsearchPort;

    private String elasticsearchScheme;

    private String slackUrl;

    private String messageEventTopic;

    private String dateSynchronizeErrorTopic;

    private String productDateRefreshTopic;

    private String eventTrackingBootstrapServers;

    private String eventTrackingGroupId;

    private int eventTrackingConsumerCount;

    private String eventTrackingElasticsearchHostname;

    private int eventTrackingElasticsearchPort;

    private String eventTrackingElasticsearchScheme;
}
