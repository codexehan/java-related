package codexe.han.kafkadatapipeline.service.Impl;

import codexe.han.kafkadatapipeline.entity.PipelineKafkaOffset;
import codexe.han.kafkadatapipeline.repository.PipelineKafkaOffsetRepository;
import codexe.han.kafkadatapipeline.service.PipelineKafkaOffsetService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PipelineKafkaOffsetServiceImpl implements PipelineKafkaOffsetService {
    private PipelineKafkaOffsetRepository pipelineKafkaOffsetRepository;
    @Override
    public PipelineKafkaOffset getKafkaOffsetByTopicName(String topicName) {
        return this.pipelineKafkaOffsetRepository.findByTopic(topicName);
    }

    @Override
    public void updateOffset(Long id, Long offset) {
        this.pipelineKafkaOffsetRepository.updateKafkaOffset(offset, id);
    }

}
