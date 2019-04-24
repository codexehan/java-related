package codexe.han.kafkadatapipeline.service;

import codexe.han.kafkadatapipeline.entity.PipelineKafkaOffset;

public interface PipelineKafkaOffsetService {
    PipelineKafkaOffset getKafkaOffsetByTopicName(String topicName);

    void updateOffset(Long id, Long offset);
}
