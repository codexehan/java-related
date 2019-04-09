package deja.fashion.datapipeline.service.Impl;

import deja.fashion.datapipeline.entity.PipelineKafkaOffset;
import deja.fashion.datapipeline.repository.PipelineKafkaOffsetRepository;
import deja.fashion.datapipeline.service.PipelineKafkaOffsetService;
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
