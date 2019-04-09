package deja.fashion.datapipeline.service;

import deja.fashion.datapipeline.entity.PipelineKafkaOffset;

public interface PipelineKafkaOffsetService {
    PipelineKafkaOffset getKafkaOffsetByTopicName(String topicName);

    void updateOffset(Long id, Long offset);
}
