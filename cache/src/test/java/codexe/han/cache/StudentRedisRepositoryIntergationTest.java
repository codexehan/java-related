package codexe.han.cache;

import codexe.han.cache.entity.Student;
import codexe.han.cache.repository.StudentRedisRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataRedisTest
public class StudentRedisRepositoryIntergationTest {


    @Autowired
    private StudentRedisRepository studentRedisRepository;

    @Test
    public void whenFindByName_thenReturnEmployee() {
        // given
        Student alex = new Student(1,"1","alex", Student.Gender.FEMALE,20);
        /*entityManager.persist(alex);
        entityManager.flush();*/

        //when
        alex = studentRedisRepository.save(alex);

        //then
        Assert.assertNotNull(alex);

    }

    @Test
    public void findById(){
        //when
        Student alex = studentRedisRepository.findById(1).get();

        // then
        assertThat(alex.getName()).isEqualTo("alex");
    }
}
