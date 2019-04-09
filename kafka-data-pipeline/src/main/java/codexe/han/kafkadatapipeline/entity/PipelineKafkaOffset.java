package deja.fashion.datapipeline.entity;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PipelineKafkaOffset extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="kafka_topic")
    private String topic;

    @Column(name="kafka_partition")
    private int partition;

    @Column(name="kafka_offset")
    private long offset;

    private int consumerType;

    private boolean isDelete;

}
