package codexe.han.kafkadatapipeline.repository;

import codexe.han.kafkadatapipeline.entity.PipelineKafkaOffset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PipelineKafkaOffsetRepository extends JpaRepository<PipelineKafkaOffset, Long> {

    PipelineKafkaOffset findByTopic(String kafkaTopic);

    @Modifying
    @Transactional(rollbackFor = {Exception.class})
    @Query(value = "update pipeline_kafka_offset t set t.kafka_offset = ?1,t.last_modified_date = now() where id = ?2",nativeQuery = true)
    void updateKafkaOffset(long offset,long id);
}
