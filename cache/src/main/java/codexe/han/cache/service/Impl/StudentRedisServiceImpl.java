package codexe.han.cache.service.Impl;

import codexe.han.cache.entity.Student;
import codexe.han.cache.repository.StudentRedisRepository;
import codexe.han.cache.service.StudentRedisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentRedisServiceImpl implements StudentRedisService {
    @Autowired
    private StudentRedisRepository studentRedisRepository;

    public void addStudent(){
        Student student = new Student(1,
                "1", "John Doe", Student.Gender.MALE, 1);
        studentRedisRepository.save(student);
    }

    public Student getStudent(Integer id){
        return studentRedisRepository.findById(id).get();
    }
}
