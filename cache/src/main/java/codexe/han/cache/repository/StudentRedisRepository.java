package codexe.han.cache.repository;

import codexe.han.cache.entity.Student;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRedisRepository extends JpaRepository<Student, Integer> {

}
