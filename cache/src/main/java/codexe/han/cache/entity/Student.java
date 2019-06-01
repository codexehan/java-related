package codexe.han.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Entity;
import java.io.Serializable;

@RedisHash("Student")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student implements Serializable {
    public enum Gender{
        MALE, FEMALE
    }
    @Id private Integer id;
    @Indexed private String externalId;
    private String name;
    private Gender gender;
    private int grade;
}
